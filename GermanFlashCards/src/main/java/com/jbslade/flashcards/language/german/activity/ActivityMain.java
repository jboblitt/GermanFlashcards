package com.jbslade.flashcards.language.german.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jbslade.flashcards.language.german.R;
import com.jbslade.flashcards.language.german.adapter.AdapterDeckNavigator;
import com.jbslade.flashcards.language.german.debug.MyLogger;
import com.jbslade.flashcards.language.german.model.Card;
import com.jbslade.flashcards.language.german.model.Deck;
import com.jbslade.flashcards.language.german.model.DeckTree;
import com.jbslade.flashcards.language.german.sensor.Accelerometer;
import com.jbslade.flashcards.language.german.sensor.AudioRecorderController;
import com.jbslade.flashcards.language.german.ui.AudioPlayView;
import com.jbslade.flashcards.language.german.ui.AudioRecordView;
import com.jbslade.flashcards.language.german.ui.FragmentFlashCardBack;
import com.jbslade.flashcards.language.german.ui.FragmentFlashCardFront;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActivityMain extends Activity
{
    private static final boolean DEBUG = true;
    public static final String KEY_MSG_TYPE = "KEY_MSG_TYPE";
    public static final short MSG_TYPE_CARD_SELECTED = 1;

    public static final String KEY_CARD_SELECTED_TITLE = "KEY_CARD_SELECTED_TITLE";
    public static final String KEY_CARD_SELECTED_NOTES = "KEY_CARD_SELECTED_NOTES";
    public static final String KEY_CARD_SELECTED_NUMBER = "KEY_CARD_SELECTED_NUMBER";

    public static final String KEY_ACTIVITY_PASS_DECK_TREE = "KEY_ACTIVITY_PASS_DECK_TREE";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    private boolean mShowingBack = false;
    private DeckTree m_deckTree;
    private Accelerometer m_accel;
    private AudioRecorderController m_audioRecordController;
    private Uri m_fileUri;
    private ImageView m_cardPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyLogger.initLogger("ActivityMain", DEBUG);

        m_deckTree = new DeckTree();
        m_deckTree.insertElement(new Deck("Verbs"), new ArrayList<String>());

        Deck ch1 = new Deck("Chapter1");
        Deck ch1Verbs = new Deck("Verbs");
        ch1Verbs.addElement(new Card("kennen"));
        ch1Verbs.addElement(new Card("leisen"));
        ch1Verbs.addElement(new Card("kommen"));
        ch1Verbs.addElement(new Card("schlafen"));
        ch1Verbs.addElement(new Card("schicken"));
        ch1Verbs.addElement(new Card("laufen"));
        ch1Verbs.addElement(new Card("besuchen"));
        ch1Verbs.addElement(new Card("gehen"));
        ch1Verbs.addElement(new Card("sein"));
        ch1.addElement(ch1Verbs);
        m_deckTree.insertElement(ch1, new ArrayList<String>());

        Deck ch2 = new Deck("Chapter2");
        ch2.addElement(new Card("grammer1"));
        ch2.addElement(new Card("grammer2"));
        ch2.addElement(new Card("grammer3"));
        Deck ch2Vocab = new Deck("Vocabulary");
        ch2Vocab.addElement(new Card("Kommode"));
        ch2Vocab.addElement(new Card("Tische"));
        ch2Vocab.addElement(new Card("Wohngemeinschaft"));
        ch2.addElement(ch2Vocab);
        m_deckTree.insertElement(ch2, new ArrayList<String>());

        ListView lv = (ListView) this.findViewById(R.id.ListView_ActivityMainNavigation);
        AdapterDeckNavigator navigator = new AdapterDeckNavigator(UI_Handler);
        navigator.setDeckTree(m_deckTree);
        lv.setAdapter(navigator);

        Fragment fragment = new FragmentFlashCardFront();
        getFragmentManager().beginTransaction()
                .add(R.id.FrameLayout_MainFragmentContainer, fragment)
                .commit();

        FrameLayout fl = (FrameLayout) this.findViewById(R.id.FrameLayout_MainFragmentContainer);
        fl.setOnClickListener(new FrameLayout.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                flipCard();
            }
        });

        m_accel = new Accelerometer(this);
        m_accel.setOnThresholdPassedListener(new Accelerometer.OnThresholdPassedListener()
        {
            @Override
            public void onThresholdPassed()
            {
                flipCard();
            }
        });

        m_cardPicture = (ImageView) findViewById(R.id.ImageView_Card_Picture);
        m_cardPicture.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // create Intent to take a picture and return control to the calling application
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                m_fileUri = getOutputMediaFileUri(); // create a file to save the image
                intent.putExtra(MediaStore.EXTRA_OUTPUT, m_fileUri); // set the image file name

                // start the image capture Intent
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        m_accel.reactivateAccelerometer();
        m_audioRecordController = new AudioRecorderController(
                (AudioRecordView) findViewById(R.id.Button_Card_RecordAudio),
                (AudioPlayView) findViewById(R.id.Button_Card_PlayAudio));
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        m_accel.deactivateAccelerometer();
        m_audioRecordController.stop();
    }

    private void flipCard()
    {
        m_accel.deactivateAccelerometer();
        // Flip to the back.
        Fragment frag = mShowingBack ? new FragmentFlashCardFront() : new FragmentFlashCardBack();

        mShowingBack = !mShowingBack;

        // Create and commit a new fragment transaction that adds the fragment for the back of
        // the card, uses custom animations, and is part of the fragment manager's back stack.

        getFragmentManager()
                .beginTransaction()

                        // Replace the default fragment animations with animator resources representing
                        // rotations when switching to the back of the card, as well as animator
                        // resources representing rotations when flipping back to the front (e.g. when
                        // the system Back button is pressed).
                .setCustomAnimations(
                        R.animator.flashcard_flip_right_in, R.animator.flashcard_flip_right_out,
                        R.animator.flashcard_flip_left_in, R.animator.flashcard_flip_left_out)

                        // Replace any fragments currently in the container view with a fragment
                        // representing the next page (indicated by the just-incremented currentPage
                        // variable).
                .replace(R.id.FrameLayout_MainFragmentContainer, frag)

                        // Add this transaction to the back stack, allowing users to press Back
                        // to get to the front of the card.
//                .addToBackStack(null)

                        // Commit the transaction.
                .commit();

        m_accel.reactivateAccelerometer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_settings:
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.action_share:
                Intent intent = new Intent(ActivityMain.this, ActivityShare.class);
                intent.putExtra(ActivityMain.KEY_ACTIVITY_PASS_DECK_TREE,
                        m_deckTree.convertThisObjToJSON().toString());
                ActivityMain.this.startActivity(intent);
                break;

            default:
                break;
        }

        return true;
    }

    private Handler UI_Handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            Bundle msgBundle = msg.getData();
            switch (msgBundle.getShort(KEY_MSG_TYPE))
            {
                case MSG_TYPE_CARD_SELECTED:
                    String title = msgBundle.getString(KEY_CARD_SELECTED_TITLE);
                    String cardNum = msgBundle.getString(KEY_CARD_SELECTED_NUMBER);
                    String notes = msgBundle.getString(KEY_CARD_SELECTED_NOTES);

                    ((TextView) ActivityMain.this.findViewById(R.id.TextView_Card_SectionTitle))
                            .setText(title);
                    ((TextView) ActivityMain.this.findViewById(R.id.TextView_Card_Number))
                            .setText(cardNum);
                    ((TextView) ActivityMain.this.findViewById(R.id.TextView_Card_Notes))
                            .setText(notes);

                    break;
                default:
                    MyLogger.logWarning(ActivityMain.class.getName(),
                            "UI_Handler handleMessage",
                            "ActivityMain Handler received non-supported message type");
                    break;
            }
        }
    };

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri()
    {
        return Uri.fromFile(getOutputMediaFile());
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile()
    {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "GermanFlashCards");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
            {
                MyLogger.logSevere(ActivityMain.class.getSimpleName(), "getOutputMediaFile",
                        "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            MyLogger.logInfo(ActivityMain.class.getSimpleName(), "onActivityResult",
                    "Received response from camera");
        }
        else if (resultCode == RESULT_CANCELED)
        {
            // User cancelled the image capture
        }
        else
        {
            // Image capture failed, advise user
        }
    }
}



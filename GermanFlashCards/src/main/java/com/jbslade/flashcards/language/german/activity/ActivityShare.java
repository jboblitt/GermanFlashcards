package com.jbslade.flashcards.language.german.activity;

import android.app.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jbslade.flashcards.language.german.R;
import com.jbslade.flashcards.language.german.adapter.AdapterControllerDecksList;
import com.jbslade.flashcards.language.german.adapter.ListBasicAdapter;
import com.jbslade.flashcards.language.german.debug.MyLogger;
import com.jbslade.flashcards.language.german.model.DeckTree;
import com.jbslade.flashcards.language.german.network.AsyncTaskPostToResource;
import com.jbslade.flashcards.language.german.network.ControllerSingleResourceType;
import com.jbslade.flashcards.language.german.network.ResourceParserDeckTree;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActivityShare extends Activity
{
    private static final short MSG_TYPE_SEND_SERVER_DECK_TREES = 1;
    private static final String KEY_SERVER_DECK_TREES = "KEY_SERVER_DECK_TREES";

    private static final String JSON_KEY_ALL_USER_DECK_TREE = "JSON_KEY_ALL_USER_DECK_TREE";
    private static final String JSON_KEY_USERNAME = "JSON_KEY_USERNAME";
    private static final String JSON_KEY_SINGLE_USER_DECK_TREE = "JSON_KEY_SINGLE_USER_DECK_TREE";

    private DeckTree m_deckTree;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Bundle intentData = getIntent().getExtras();
        if (intentData != null)
        {
            try
            {
                JSONObject jsonDeckTree = new JSONObject(
                        intentData.getString(ActivityMain.KEY_ACTIVITY_PASS_DECK_TREE));
                m_deckTree = new DeckTree(jsonDeckTree);
            }
            catch (JSONException e)
            {
                MyLogger.logExceptionSevere(ActivityShare.class.getSimpleName(), "onCreate()", "",
                        e);
            }
        }


        SharedPreferences myPrefs = getMyPreferences();
        ((EditText) findViewById(R.id.EditText_ActivityShare_Username))
                .setText(myPrefs.getString(Preferences.PREF_ACTIVITY_SHARE_USERNAME, null));
        ((EditText) findViewById(R.id.EditText_ActivityShare_Port))
                .setText(myPrefs.getString(Preferences.PREF_ACTIVITY_SHARE_PORT, null));


        findViewById(R.id.Button_ActivityShare_Port).setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String userName = ((EditText) findViewById(
                                R.id.EditText_ActivityShare_Username)).getText().toString();
                        String port = ((EditText) findViewById(
                                R.id.EditText_ActivityShare_Port)).getText().toString();

                        SharedPreferences.Editor editor = getMyPreferences().edit();
                        editor.putString(Preferences.PREF_ACTIVITY_SHARE_USERNAME, userName);
                        editor.putString(Preferences.PREF_ACTIVITY_SHARE_PORT, port);
                        editor.commit();

                        String path =
                                "http://10.0.2.2:" + port + "/userdecktree?username=" + userName;
                        AsyncTaskPostToResource asyncPost =
                                new AsyncTaskPostToResource(path, m_deckTree);
                        asyncPost.execute();
                    }
                });


        findViewById(R.id.Button_ActivityShare_UpdateOtherDecks).setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        //Set up rss URLs to fetch and parse by single parser
                        String port = ((EditText) findViewById(
                                R.id.EditText_ActivityShare_Port)).getText().toString();
                        String[] serverUrl = {"http://10.0.2.2:" + port + "/userdecktree"};

                        //Set up content/error handler that will be used during parsing of the rss resource.

                        ResourceParserDeckTree resourceParserDeckTree =
                                new ResourceParserDeckTree();

                        ControllerSingleResourceType<String> serverDeckTreeRetriever =
                                new ControllerSingleResourceType<String>(resourceParserDeckTree,
                                        UI_Handler,
                                        ActivityMain.KEY_MSG_TYPE, MSG_TYPE_SEND_SERVER_DECK_TREES,
                                        KEY_SERVER_DECK_TREES, serverUrl);

                        serverDeckTreeRetriever.fetchAndParseResources();
                    }
                });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_share, menu);
        return true;
    }

    private Handler UI_Handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            Bundle msgBundle = msg.getData();
            switch (msgBundle.getShort(ActivityMain.KEY_MSG_TYPE))
            {
                case MSG_TYPE_SEND_SERVER_DECK_TREES:
                    MyLogger.logInfo(ActivityMain.class.getName(),
                            "UI_Handler handleMessage",
                            "ActivityMain Handler received deck trees message");
                    ArrayList<String> serverResponses =
                            (ArrayList<String>) msgBundle.getSerializable(KEY_SERVER_DECK_TREES);
                    ArrayList<DeckTree> parsedTrees = new ArrayList<DeckTree>();
                    ArrayList<String> userNames = new ArrayList<String>();
                    for (String strResp : serverResponses)
                    {
                        try
                        {
                            JSONObject json = new JSONObject(strResp);
                            JSONArray jsonArrayAllUserTrees =
                                    json.getJSONArray(JSON_KEY_ALL_USER_DECK_TREE);
                            for (int i = 0; i < jsonArrayAllUserTrees.length(); i++)
                            {
                                JSONObject userTree = jsonArrayAllUserTrees.getJSONObject(i);
                                String strDeckTree =
                                        userTree.getString(JSON_KEY_SINGLE_USER_DECK_TREE);
                                parsedTrees.add(new DeckTree(new JSONObject(strDeckTree)));
                                userNames.add(userTree.getString(JSON_KEY_USERNAME));
                            }
                        }
                        catch (JSONException e)
                        {
                            MyLogger.logExceptionSevere(
                                    ResourceParserDeckTree.class.getSimpleName(),
                                    "getResourceParsedElems()", "", e);
                        }
                    }

                    //Need to refactor this code.  Way to chunky.
                    ListView lv = (ListView) ActivityShare.this
                            .findViewById(R.id.ListView_ActivityShare_OtherDecks);
                    ListBasicAdapter basicAdapter = new ListBasicAdapter();
                    AdapterControllerDecksList adapterControllerDecksList =
                            new AdapterControllerDecksList();
                    adapterControllerDecksList.setUserNames(userNames);
                    adapterControllerDecksList.addElemsToList(parsedTrees);
                    basicAdapter.setAdapterController(adapterControllerDecksList);
                    lv.setAdapter(basicAdapter);

                    Date currTime = new Date(System.currentTimeMillis());
                    String dateTime = "Last updated: " +
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currTime);
                    ((TextView) ActivityShare.this
                            .findViewById(R.id.TextView_ActivityShare_LastUpdate))
                            .setText(dateTime);


                    break;
                default:
                    MyLogger.logWarning(ActivityMain.class.getName(),
                            "UI_Handler handleMessage",
                            "ActivityMain Handler received non-supported message type");
                    break;
            }
        }
    };


    public SharedPreferences getMyPreferences()

    {
        return getSharedPreferences(Preferences.APP_PREFERENCES, Context.MODE_PRIVATE);
    }
}

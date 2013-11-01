package com.jbslade.flashcards.language.german.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.jbslade.flashcards.language.german.interfaces.AudioStateInterface;


/**
 * Created by Justin on 10/31/13.
 */
public class AudioRecordView extends Button implements AudioStateInterface
{
    public AudioRecordView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        onAudioStateChanged(false);
    }

    @Override
    public void onAudioStateChanged(boolean isActive)
    {
        if (isActive)
        {
            setText("Stop recording");
        }
        else
        {
            setText("Start recording");
        }
    }
}

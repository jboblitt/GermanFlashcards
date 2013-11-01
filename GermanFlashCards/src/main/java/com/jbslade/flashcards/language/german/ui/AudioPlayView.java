package com.jbslade.flashcards.language.german.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.jbslade.flashcards.language.german.interfaces.AudioStateInterface;


/**
 * Created by Justin on 10/31/13.
 */
public class AudioPlayView extends Button implements AudioStateInterface
{
    public AudioPlayView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        onAudioStateChanged(false);
    }

    @Override
    public void onAudioStateChanged(boolean isActive)
    {
        if (isActive)
        {
            setText("Stop playing");
        }
        else
        {
            setText("Start playing");
        }
    }
}

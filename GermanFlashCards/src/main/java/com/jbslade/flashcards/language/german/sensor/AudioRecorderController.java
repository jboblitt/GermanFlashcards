package com.jbslade.flashcards.language.german.sensor;

import android.view.View;

import com.jbslade.flashcards.language.german.interfaces.AudioStateInterface;
import com.jbslade.flashcards.language.german.ui.AudioPlayView;
import com.jbslade.flashcards.language.german.ui.AudioRecordView;

/**
 * Created by Justin on 10/31/13.
 */
public class AudioRecorderController
{
    private final AudioRecordView m_recordView;
    private final AudioPlayView m_playView;
    private AudioRecorder m_recorder;
    boolean m_isRecording;
    boolean m_isPlaying;


    public AudioRecorderController(AudioRecordView recordView, AudioPlayView playView)
    {
        m_recorder = new AudioRecorder("filename");
        m_isRecording = false;
        m_isPlaying = false;
        m_recordView = recordView;
        m_playView = playView;

        m_recordView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                m_isRecording = !m_isRecording;
                updateRecordState();
            }
        });

        m_playView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                m_isPlaying = !m_isPlaying;
                updatePlayState();
            }
        });
    }

    public void stop()
    {
        m_isRecording = false;
        m_isPlaying = false;
        m_recorder.releaseAudioRecorder();
        m_recorder.releaseAudioPlayer();
    }

    public void updateRecordState()
    {
        m_recorder.onRecordStateChange(m_isRecording);
        m_recordView.onAudioStateChanged(m_isRecording);
    }

    public void updatePlayState()
    {
        m_recorder.onPlayStateChange(m_isPlaying);
        m_playView.onAudioStateChanged(m_isPlaying);
    }


}

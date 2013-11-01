package com.jbslade.flashcards.language.german.sensor;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;

import com.jbslade.flashcards.language.german.debug.MyLogger;

import java.io.IOException;

/**
 * Created by Justin on 10/31/13.
 */
public class AudioRecorder
{
    private static final String fileNamePrefix = "card_audio_";
    private String m_fileName;

    private MediaRecorder m_recorder;
    private MediaPlayer m_player;

    public AudioRecorder(String fileName)
    {
        m_fileName =
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileNamePrefix +
                        fileName + ".3gp";
    }

    public void onRecordStateChange(boolean start)
    {
        if (start)
        {
            startRecording();
        }
        else
        {
            stopRecording();
        }
    }

    public void onPlayStateChange(boolean start)
    {
        if (start)
        {
            startPlaying();
        }
        else
        {
            stopPlaying();
        }
    }

    private void startPlaying()
    {
        m_player = new MediaPlayer();
        try
        {
            m_player.setDataSource(m_fileName);
            m_player.prepare();
            m_player.start();
        }
        catch (IOException e)
        {
            MyLogger.logExceptionSevere(
                    AudioRecorder.class.getSimpleName(), "startPlaying", "prepare() failed", e);
        }
    }

    private void stopPlaying()
    {
        m_player.release();
        m_player = null;
    }

    private void startRecording()
    {
        m_recorder = new MediaRecorder();
        m_recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        m_recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        m_recorder.setOutputFile(m_fileName);
        m_recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try
        {
            m_recorder.prepare();
        }
        catch (IOException e)
        {
            MyLogger.logExceptionSevere(
                    AudioRecorder.class.getSimpleName(), "startPlaying", "prepare() failed", e);
        }

        m_recorder.start();
    }

    private void stopRecording()
    {
        m_recorder.stop();
        m_recorder.release();
        m_recorder = null;
    }

    public void releaseAudioRecorder()
    {
        if (m_recorder != null)
        {
            m_recorder.release();
            m_recorder = null;
        }
    }

    public void releaseAudioPlayer()
    {
        if (m_player != null)
        {
            m_player.release();
            m_player = null;
        }
    }


}

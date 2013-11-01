package com.jbslade.flashcards.language.german.network;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.jbslade.flashcards.language.german.debug.MyLogger;
import com.jbslade.flashcards.language.german.interfaces.JSONInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Justin on 10/2/13.
 */
public class AsyncTaskParseResource<T> extends AsyncTask<Object, Void, Void>
{
    private ArrayBlockingQueue<ResourceStream> m_inputBlockingQueue;
    private ResourceParser m_resourceParser;
    private Handler m_handler;
    private final String m_keyMsgType;
    private final short m_msgType;
    private final String m_keyPayload;


    public AsyncTaskParseResource(ArrayBlockingQueue<ResourceStream> inputProcessingQueue,
                                  ResourceParser<T> resourceParser,
                                  Handler handlerToSendParsedElems,
                                  String keyMsgType, short msgType, String keyPayload)

    {
        m_inputBlockingQueue = inputProcessingQueue;
        m_resourceParser = resourceParser;
        m_handler = handlerToSendParsedElems;
        m_keyMsgType = keyMsgType;
        m_msgType = msgType;
        m_keyPayload = keyPayload;
    }

    @Override
    protected Void doInBackground(Object... params)
    {
        try
        {
            ArrayList<T> elementsParsed = new ArrayList<T>();
            ResourceStream resStream;
            while (!(resStream = m_inputBlockingQueue.take()).isDoneFetching())
            {
                if (!isCancelled())
                {
                    elementsParsed.addAll(m_resourceParser
                            .getResourceParsedElems(resStream.getInputStream()));
                }
            }

            sendParsedElemsToHandler(elementsParsed);
        }

        catch (InterruptedException e)
        {
            MyLogger.logExceptionSevere(AsyncTaskParseResource.class.getName(), "doInBackground",
                    null, e);
        }

        return null;
    }

    private void sendParsedElemsToHandler(ArrayList<T> elementsParsed)
    {
        Bundle bundle = new Bundle();
        if (m_keyMsgType != null)
        {
            bundle.putShort(m_keyMsgType, m_msgType);
        }
        if (m_keyPayload != null && elementsParsed != null)
        {
            bundle.putSerializable(m_keyPayload, elementsParsed);
        }
        Message msg = new Message();
        msg.setData(bundle);
        m_handler.sendMessage(msg);
    }


}

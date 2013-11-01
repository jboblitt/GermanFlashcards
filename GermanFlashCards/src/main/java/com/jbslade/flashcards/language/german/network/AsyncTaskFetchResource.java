package com.jbslade.flashcards.language.german.network;

import android.os.AsyncTask;

import com.jbslade.flashcards.language.german.debug.MyLogger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Justin on 10/2/13.
 */
public class AsyncTaskFetchResource extends AsyncTask<String, Void, Void>
{
    ArrayBlockingQueue<ResourceStream> m_blockingQueue;

    public AsyncTaskFetchResource(ArrayBlockingQueue<ResourceStream> blockingQueue)
    {
        m_blockingQueue = blockingQueue;
    }

    @Override
    protected Void doInBackground(String... urls)
    {
        if (urls == null)
            return null;
        try
        {
            for (int i = 0; i < urls.length; i++)
            {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(urls[i]);
                HttpResponse response = client.execute(request);
                InputStream is = response.getEntity().getContent();
                ResourceStream source = new ResourceStream(is);
                m_blockingQueue.put(source);
            }
            m_blockingQueue.put(new ResourceStream());
        }
        catch (IOException e)
        {
            MyLogger.logExceptionSevere(AsyncTaskFetchResource.class.getName(), "doInBackground",
                    null, e);
        }
        catch (InterruptedException e)
        {
            MyLogger.logExceptionSevere(AsyncTaskFetchResource.class.getName(), "doInBackground",
                    null, e);
        }
        return null;
    }
}

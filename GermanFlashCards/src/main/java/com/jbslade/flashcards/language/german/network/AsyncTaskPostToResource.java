package com.jbslade.flashcards.language.german.network;

import android.os.AsyncTask;

import com.jbslade.flashcards.language.german.interfaces.JSONInterface;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Justin on 10/2/13.
 */
public class AsyncTaskPostToResource extends AsyncTask<Object, Void, Void>
{
    private final String m_path;
    private final JSONInterface m_objToPost;
/*
    private final Handler m_handler;
    private final String m_keyMsgType;
    private final short m_msgType;
    private final String m_keyResponse;
*/

    public AsyncTaskPostToResource(/*Handler handler, String keyMsgType, short msgType,
                                   String keyResponse*/String path, JSONInterface objToPost)
    {
        /*m_handler = handler;
        m_keyMsgType = keyMsgType;
        m_msgType = msgType;
        m_keyResponse = keyResponse;*/
        m_path = path;
        m_objToPost = objToPost;
    }


    @Override
    protected Void doInBackground(Object... obj)
    {
        try
        {
            //instantiates httpclient to make request
            DefaultHttpClient httpclient = new DefaultHttpClient();

            //url with the post data
            HttpPost httpost = new HttpPost(m_path);

            JSONObject json = m_objToPost.convertThisObjToJSON();

            //passes the results to a string builder/entity
            StringEntity se = new StringEntity(json.toString());

            //sets the post request as the resulting string
            httpost.setEntity(se);
            //sets a request header so the page receving the request
            //will know what to do with it
            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");

            //Handles what is returned from the page
            ResponseHandler responseHandler = new BasicResponseHandler();

            httpclient.execute(httpost, responseHandler);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}

package com.jbslade.flashcards.language.german.network;


import android.os.Handler;

import com.jbslade.flashcards.language.german.interfaces.JSONInterface;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Justin on 10/2/13.
 */
public class ControllerSingleResourceType<T>
{
    private String[] m_listUrls;
    private AsyncTaskFetchResource asyncFetchHtml;
    private AsyncTaskParseResource<T> asyncParseHtml;


    public ControllerSingleResourceType(ResourceParser<T> resourceParser,
                                        Handler handlerToSendParsedElems, String keyMsgType,
                                        short msgType, String keyPayload, String... listUrl)
    {
        ArrayBlockingQueue<ResourceStream> blockingQueue =
                new ArrayBlockingQueue<ResourceStream>(listUrl.length + 1); //+1 because DONE signal
        asyncFetchHtml = new AsyncTaskFetchResource(blockingQueue);
        asyncParseHtml = new AsyncTaskParseResource<T>(blockingQueue, resourceParser,
                handlerToSendParsedElems, keyMsgType, msgType, keyPayload);
        m_listUrls = listUrl;
    }

    public void fetchAndParseResources()
    {
        asyncFetchHtml.execute(m_listUrls);
        asyncParseHtml.execute();
    }
}

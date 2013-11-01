package com.jbslade.flashcards.language.german.network;

import java.io.InputStream;

/**
 * Created by Justin on 10/7/13.
 */
public class ResourceStream
{
    private InputStream m_inputStream;
    private boolean m_doneFetchingFlag;

    public ResourceStream(InputStream is)
    {
        m_inputStream = is;
        m_doneFetchingFlag = false;
    }

    public ResourceStream()
    {
        m_doneFetchingFlag = true;
    }

    public boolean isDoneFetching()
    {
        return m_doneFetchingFlag;
    }

    public InputStream getInputStream()
    {
        return m_inputStream;
    }
}

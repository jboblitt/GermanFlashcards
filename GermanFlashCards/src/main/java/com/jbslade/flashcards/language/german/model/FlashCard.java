package com.jbslade.flashcards.language.german.model;

import com.jbslade.flashcards.language.german.interfaces.FlashCardInterface;
import com.jbslade.flashcards.language.german.interfaces.JSONInterface;
import com.jbslade.flashcards.language.german.interfaces.eFlashCard_Side;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Justin on 9/9/13.
 */
public class FlashCard implements FlashCardInterface
{
    private String m_frontMsg;
    private String m_backMsg;

    public FlashCard()
    {
        m_frontMsg = "Flash card front.";
        m_backMsg = "Flash card back.";
    }

    @Override
    public void setMsg(eFlashCard_Side side, String msg)
    {
        switch (side)
        {
            case FRONT:
                this.m_frontMsg = msg;
                break;
            case BACK:
                this.m_backMsg = msg;
                break;
            default:
                break;
        }
    }

    @Override
    public void pronounceMsg(eFlashCard_Side side)
    {
        switch (side)
        {
            case FRONT:
                break;
            case BACK:
                break;
            default:
                break;
        }
    }

    @Override
    public String getMsg(eFlashCard_Side side)
    {
        switch (side)
        {
            case FRONT:
                return this.m_frontMsg;
            case BACK:
                return this.m_backMsg;
            default:
                return "Flash card message.";
        }

    }
}

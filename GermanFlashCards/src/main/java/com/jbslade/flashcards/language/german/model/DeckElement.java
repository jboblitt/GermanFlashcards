package com.jbslade.flashcards.language.german.model;

import com.jbslade.flashcards.language.german.interfaces.DeckElementInterface;

import org.json.JSONObject;

/**
 * Created by Justin on 9/22/13.
 */
public class DeckElement implements DeckElementInterface<String>
{
    public static final String JSON_KEY_NAME = "name";
    public static final String JSON_KEY_ELEM_TYPE = "type";

    private String m_name;
    private String m_key;

    public DeckElement(String name)
    {
        m_name = name;
        m_key = getKeyFromElemName(name);
    }

    public DeckElement(JSONObject json)
    {

    }

    @Override
    public String getKey()
    {
        return m_key == null ? "HOME" : m_key;
    }

    @Override
    public String getName()
    {
        return m_name;
    }

    @Override
    public String getKeyFromElemName(String name)
    {
        return "key_" + name.replaceAll(" ", "_").trim().toLowerCase();
    }


}

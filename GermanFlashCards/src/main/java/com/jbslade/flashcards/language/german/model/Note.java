package com.jbslade.flashcards.language.german.model;

import com.jbslade.flashcards.language.german.interfaces.NoteInterface;

/**
 * Created by Justin on 9/20/13.
 */
public class Note implements NoteInterface
{
    private String m_text;

    public Note()
    {
        m_text = "My notes to help me remember.";
    }

    @Override
    public String getText()
    {
        return this.m_text;
    }

    @Override
    public void setText(String text)
    {
        this.m_text = text;
    }
}

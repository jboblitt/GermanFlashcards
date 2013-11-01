package com.jbslade.flashcards.language.german.model;

import com.jbslade.flashcards.language.german.debug.MyLogger;
import com.jbslade.flashcards.language.german.interfaces.CardInterface;
import com.jbslade.flashcards.language.german.interfaces.JSONInterface;
import com.jbslade.flashcards.language.german.interfaces.NoteInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Justin on 9/20/13.
 */
public class Card extends DeckElement implements CardInterface<Integer>, JSONInterface
{
    public static final int MAX_DIFFICULTY = 10;

    private FlashCard m_flashCard;
    private ArrayList<NoteInterface> m_notes;
    private int m_difficulty;

    public Card(String name)
    {
        super(name != null ? name : "New Card");
        m_flashCard = new FlashCard();
        m_notes = new ArrayList<NoteInterface>();
        m_difficulty = MAX_DIFFICULTY / 2;
    }

    public Card(JSONObject json) throws JSONException
    {
        this(json.getString(JSON_KEY_NAME));
    }


    @Override
    public FlashCard getFlashCard()
    {
        return m_flashCard;
    }

    @Override
    public Collection<NoteInterface> getNotes()
    {
        return m_notes;
    }

    @Override
    public void addNote(NoteInterface note)
    {
        this.m_notes.add(note);
    }

    @Override
    public NoteInterface removeNote(Integer noteIndex)
    {
        return this.m_notes.remove(noteIndex.intValue());
    }

    @Override
    public void setDifficulty(int difficulty)
    {
        this.m_difficulty = difficulty;
    }

    @Override
    public int getDifficulty()
    {
        return this.m_difficulty;
    }

    @Override
    public JSONObject convertThisObjToJSON()
    {
        JSONObject json = new JSONObject();
        try
        {
            json.put(JSON_KEY_NAME, getName());
            json.put(JSON_KEY_ELEM_TYPE, Card.class.getSimpleName());
        }
        catch (JSONException e)
        {
            MyLogger.logExceptionSevere(Card.class.getName(), "convertThisObjToJSON", "", e);
        }
        catch (Exception e)
        {
            MyLogger.logExceptionSevere(Card.class.getName(), "convertThisObjToJSON", "", e);
        }
        return json;
    }

}

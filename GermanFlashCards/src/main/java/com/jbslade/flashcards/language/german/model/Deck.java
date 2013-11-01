package com.jbslade.flashcards.language.german.model;

import com.jbslade.flashcards.language.german.debug.MyLogger;
import com.jbslade.flashcards.language.german.interfaces.DeckInterface;
import com.jbslade.flashcards.language.german.interfaces.JSONInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

/**
 * Created by Justin on 9/20/13.
 */
public class Deck extends DeckElement implements DeckInterface<String>, JSONInterface
{
    public static final String JSON_KEY_ELEMENTS = "elements";

    private Hashtable<String, DeckElement> m_elements;

    public Deck(String name)
    {
        super(name != null ? name : "New Deck");
        m_elements = new Hashtable<String, DeckElement>();
    }

    public Deck(JSONObject json) throws JSONException
    {
        this(json.getString(JSON_KEY_NAME));

        //Iterate through JSON elements that belong directly to this deck
        JSONArray jsonElems = json.getJSONArray(JSON_KEY_ELEMENTS);
        for (int i = 0; i < jsonElems.length(); i++)
        {
            JSONObject innerJSONElem = jsonElems.getJSONObject(i);
            if (innerJSONElem.getString(JSON_KEY_ELEM_TYPE).equals(Deck.class.getSimpleName()))
            {
                //If inner JSON element is a deck. Construct a deck out of the object.
                this.addElement(new Deck(innerJSONElem));
            }
            else
            {
                //else this JSON element is a Card.  Construct card
                this.addElement(new Card(innerJSONElem));
            }
        }
    }

    @Override
    public int getNumElements()
    {
        //number of elements in this object's 1st level
        return m_elements.size();
    }

    @Override
    public Collection<Deck> getDecks()
    {
        ArrayList<Deck> decks = new ArrayList<Deck>();
        for (DeckElement elem : m_elements.values())
            if (elem instanceof Deck)
                decks.add((Deck) elem);
        return decks;
    }

    @Override
    public Collection<Card> getCards()
    {
        ArrayList<Card> cards = new ArrayList<Card>();
        for (DeckElement elem : m_elements.values())
            if (elem instanceof Card)
                cards.add((Card) elem);
        return cards;
    }

    @Override
    public ArrayList<DeckElement> getElements()
    {
        ArrayList<DeckElement> all = new ArrayList<DeckElement>();
        for (DeckElement elem : m_elements.values())
            all.add(elem);
        return all;
    }

    @Override
    public DeckElement getElementByKey(String key)
    {
        return this.m_elements.get(key);
    }

    @Override
    public DeckElement getElementByName(String name)
    {
        return this.m_elements.get(getKeyFromElemName(name));
    }

    @Override
    public void addElement(DeckElement elem)
    {
        this.m_elements.put(elem.getKey(), elem);
    }

    /**
     * @param elemToMerge - element to add to this deck
     * @return
     */
    @Override
    public boolean addAllElems(Deck elemToMerge)
    {
        boolean success = true;
        for (DeckElement elem : elemToMerge.getElements())
        {
            if (this.contains(elem))
                success = false;
            else
                addElement(elem);
        }
        return success;
    }

    @Override
    public DeckElement removeElement(DeckElement elem)
    {
        return this.m_elements.remove(elem.getKey());
    }

    @Override
    public DeckElement removeElementByName(String elemName)
    {
        return this.m_elements.remove(getKeyFromElemName(elemName));
    }


    @Override
    public boolean handleDuplicateInSet(DeckElement insertingElem)
    {
        DeckElement origElem = this.getElementByKey(insertingElem.getKey());
        if (origElem instanceof Card && insertingElem instanceof Card)
        {
            return false;
        }
        else if (origElem instanceof Deck && insertingElem instanceof Card)
        {
            //Merging card with a deck.  Add card to original collection.
            ((Deck) origElem).addElement(insertingElem);
        }
        else if (origElem instanceof Card && insertingElem instanceof Deck)
        {
            //Card is in place with same key as the deck we're inserting.
            this.removeElement(origElem);
            ((Deck) insertingElem).addElement(origElem);
            this.addElement(insertingElem);
        }
        else
        {
            //Both are decks. Take the elems from the inserting deck and add them to original.
            ((Deck) origElem).addAllElems((Deck) insertingElem);
        }
        return true;
    }

    @Override
    public boolean contains(DeckElement elem)
    {
        return this.m_elements.get(elem.getKey()) != null;
    }

    @Override
    public boolean contains(String elemName)
    {
        return this.m_elements.get(getKeyFromElemName(elemName)) != null;
    }

    @Override
    public JSONObject convertThisObjToJSON()
    {
        JSONObject json = new JSONObject();
        try
        {
            JSONArray arrayElements = new JSONArray();

            // If any are null than put JSONObjectNULL
            for (DeckElement elem : getElements())
            {
                if (elem instanceof Deck)
                {
                    arrayElements.put(((Deck) elem).convertThisObjToJSON());
                }
                else
                {
                    arrayElements.put(((Card) elem).convertThisObjToJSON());
                }
            }
            json.put(JSON_KEY_ELEMENTS, arrayElements);
            json.put(JSON_KEY_NAME, getName());
            json.put(JSON_KEY_ELEM_TYPE, Deck.class.getSimpleName());
        }
        catch (JSONException e)
        {
            MyLogger.logExceptionSevere(Deck.class.getName(), "convertThisObjToJSON", "", e);
        }
        catch (Exception e)
        {
            MyLogger.logExceptionSevere(Deck.class.getName(), "convertThisObjToJSON", "", e);
        }

        return json;
    }

}

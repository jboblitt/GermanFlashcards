package com.jbslade.flashcards.language.german.model;

import com.jbslade.flashcards.language.german.debug.MyLogger;
import com.jbslade.flashcards.language.german.interfaces.JSONInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Justin on 10/25/13.
 */
public class DeckTree implements JSONInterface
{
    private Deck root;

    public DeckTree()
    {
        root = new Deck("ROOT");
    }

    public DeckTree(JSONObject json) throws JSONException
    {
        root = new Deck(json);
    }


    public Collection<DeckElement> findAndReturnDeckElements(ArrayList<String> deckPath)
    {
        return findAndReturnDeckElements(root, deckPath);
    }

    private Collection<DeckElement> findAndReturnDeckElements(Deck deck,
                                                              ArrayList<String> deckKeyPath)
    {
        if (deckKeyPath.isEmpty())
        {
            //Element already exists (have same names), merge the two.
            return deck.getElements();
        }
        else
        {
            Deck nextDeckInPath = (Deck) deck.getElementByName(deckKeyPath.remove(0));
            return findAndReturnDeckElements(nextDeckInPath, deckKeyPath);
        }
    }

    public DeckElement removeElement(ArrayList<String> directPath)
    {
        return removeElement(root, directPath);
    }

    private DeckElement removeElement(Deck deck, ArrayList<String> path)
    {
        if (path.size() == 1)
        {
            if (deck.contains(path.get(0)))
            {
                //Element already exists (have same names), merge the two.
                return deck.removeElementByName(path.remove(0));
            }
            else
            {
                return null;
            }
        }
        else
        {
            Deck nextDeckInPath = (Deck) deck.getElementByName(path.remove(0));
            return removeElement(nextDeckInPath, path);
        }
    }


    public boolean insertElement(DeckElement elemToInsert,
                                 ArrayList<String> path)
    {
        return insertElement(root, elemToInsert, path);
    }


    private boolean insertElement(Deck deck, DeckElement elemToInsert, ArrayList<String> path)
    {
        if (path.isEmpty())
        {
            if (deck.contains(elemToInsert))
            {
                //Element already exists (have same names), merge the two.
                return deck.handleDuplicateInSet(elemToInsert);
            }
            else
            {
                deck.addElement(elemToInsert);
                return true;
            }
        }
        else
        {
            Deck nextDeckInPath = (Deck) deck.getElementByName(path.remove(0));
            return insertElement(nextDeckInPath, elemToInsert, path);
        }
    }

    @Override
    public JSONObject convertThisObjToJSON()
    {
        return root.convertThisObjToJSON();
    }
}

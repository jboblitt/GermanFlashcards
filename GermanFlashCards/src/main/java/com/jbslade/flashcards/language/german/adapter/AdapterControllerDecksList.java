package com.jbslade.flashcards.language.german.adapter;

import android.widget.ImageView;


import com.jbslade.flashcards.language.german.interfaces.ListBasicAdapterControllerInterface;
import com.jbslade.flashcards.language.german.model.DeckElement;
import com.jbslade.flashcards.language.german.model.DeckTree;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Justin on 10/12/13.
 */
public class AdapterControllerDecksList implements ListBasicAdapterControllerInterface<DeckTree>
{
    private ArrayList<DeckTree> m_deckTrees;
    private ArrayList<String> m_userNames;

    public AdapterControllerDecksList()
    {
        m_deckTrees = new ArrayList<DeckTree>();
    }

    public void setUserNames(ArrayList<String> userNames)
    {
        m_userNames = userNames;
    }

    @Override
    public boolean addElemsToList(ArrayList<DeckTree> elems)
    {
        return m_deckTrees.addAll(elems);
    }

    @Override
    public int getCount()
    {
        return m_deckTrees.size();
    }

    @Override
    public DeckTree getItem(int position)
    {
        //Each item in this list is actually a sentence from the story.
        return m_deckTrees.get(position);
    }

    @Override
    public String getHeader1Text(int position)
    {
        return m_userNames.get(position);
    }

    @Override
    public String getHeader2aText(int position)
    {
        return null;
    }

    @Override
    public String getHeader2bText(int position)
    {
        String header = "Deck Level 1: ";
        ArrayList<String> path = new ArrayList<String>();
        for (DeckElement elem : m_deckTrees.get(position).findAndReturnDeckElements(path))
        {
            header += elem.getName() + ", ";
        }
        header = header.substring(0, header.lastIndexOf(", "));
        return header;
    }

    @Override
    public void populateIcon(ImageView icon)
    {

    }
}

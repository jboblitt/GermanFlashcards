package com.jbslade.flashcards.language.german.interfaces;

import com.jbslade.flashcards.language.german.model.Card;
import com.jbslade.flashcards.language.german.model.Deck;
import com.jbslade.flashcards.language.german.model.DeckElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

/**
 * A deck can hold multiple decks and cards.
 */
public interface DeckInterface<K>
{
    /*Return number of elements in first level this deck*/
    public int getNumElements();

    //Returns all decks contained in 1st level of this deck.
    public Collection<Deck> getDecks();

    //Returns all cards contained in 1st level of this deck
    public Collection<Card> getCards();

    //Returns all elements contained in 1st level of this deck
    public List<DeckElement> getElements();

    //Return an element based on key
    public DeckElement getElementByKey(K key);

    public DeckElement getElementByName(String name);

    public void addElement(DeckElement elem);

    public boolean addAllElems(Deck elemToMerge);

    public DeckElement removeElement(DeckElement elem);

    public DeckElement removeElementByName(String elem);

    public boolean handleDuplicateInSet(DeckElement insertingElem);

    public boolean contains(DeckElement elem);

    public boolean contains(String elemName);
}

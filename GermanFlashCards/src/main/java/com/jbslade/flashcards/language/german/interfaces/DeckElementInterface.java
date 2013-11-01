package com.jbslade.flashcards.language.german.interfaces;

import java.io.Serializable;

public interface DeckElementInterface<K> extends Serializable
{
    public K getKey();

    public String getName();

    public String getKeyFromElemName(String name);
}

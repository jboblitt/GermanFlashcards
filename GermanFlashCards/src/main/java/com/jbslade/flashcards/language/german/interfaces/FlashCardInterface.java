package com.jbslade.flashcards.language.german.interfaces;

/**
 * A flash card has a front and a back message, each which can be pronounced.
 */
public interface FlashCardInterface
{
    public String getMsg(eFlashCard_Side side);

    public void setMsg(eFlashCard_Side side, String msg);

    public void pronounceMsg(eFlashCard_Side side);
}


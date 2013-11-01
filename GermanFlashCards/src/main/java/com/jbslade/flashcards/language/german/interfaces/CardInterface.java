package com.jbslade.flashcards.language.german.interfaces;

import com.jbslade.flashcards.language.german.model.FlashCard;

import java.util.Collection;

/**
 * A Card will have a physical flash card, collection of notes, and difficulty as set by the user.
 */
public interface CardInterface<K>
{
    public FlashCard getFlashCard();

    public Collection<NoteInterface> getNotes();

    public void addNote(NoteInterface note);

    public NoteInterface removeNote(K noteKey);

    public void setDifficulty(int difficulty);

    public int getDifficulty();
}

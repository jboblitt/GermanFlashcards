package com.jbslade.flashcards.language.german.ui;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.jbslade.flashcards.language.german.model.Deck;

/**
 * Created by Justin on 10/28/13.
 */
public class DeckView extends DeckElementView
{
    public DeckView(Context context, Deck elem, int rowNum)
    {
        super(context, elem, rowNum);
        this.setText(elem.getName());
    }


}

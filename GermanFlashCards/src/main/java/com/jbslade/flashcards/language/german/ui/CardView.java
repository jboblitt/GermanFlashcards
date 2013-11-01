package com.jbslade.flashcards.language.german.ui;

import android.content.Context;
import android.view.View;

import com.jbslade.flashcards.language.german.R;
import com.jbslade.flashcards.language.german.model.Card;

/**
 * Created by Justin on 10/28/13.
 */
public class CardView extends DeckElementView
{
    public CardView(Context context, Card elem, int rowNum)
    {
        super(context, elem, rowNum);
        this.setText(elem.getName());
        this.setTextColor(context.getResources().getColor(R.color.blue));
    }
}

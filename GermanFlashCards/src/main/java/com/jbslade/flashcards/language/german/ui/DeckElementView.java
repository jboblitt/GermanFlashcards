package com.jbslade.flashcards.language.german.ui;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jbslade.flashcards.language.german.model.Deck;
import com.jbslade.flashcards.language.german.model.DeckElement;

/**
 * Created by Justin on 10/28/13.
 */
public class DeckElementView extends TextView
{
    public static final String keyRowNum = "bundle_row_num";
    public static final String keyElemName = "bundle_elem_name";


    public DeckElementView(Context context, DeckElement elem, int rowNum)
    {
        super(context);
        this.setDeckElemTag(elem, rowNum);
    }

    public void setDeckElemTag(DeckElement elem, int rowNum)
    {
        Bundle bundle = new Bundle();
        bundle.putInt(keyRowNum, rowNum);
        bundle.putString(keyElemName, elem.getName());
        this.setTag(bundle);
        this.setLayoutParams(getMyLayoutParams());
    }

    public int getRowOfElemClicked()
    {
        return ((Bundle) this.getTag()).getInt(keyRowNum);
    }

    public String getNameOfElemClicked()
    {
        return ((Bundle) this.getTag()).getString(keyElemName);
    }

    public LinearLayout.LayoutParams getMyLayoutParams()
    {
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 10, 15, 10);
        return params;
    }


}

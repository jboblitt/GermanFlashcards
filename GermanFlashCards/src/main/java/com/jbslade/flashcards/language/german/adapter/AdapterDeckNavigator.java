package com.jbslade.flashcards.language.german.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jbslade.flashcards.language.german.R;
import com.jbslade.flashcards.language.german.activity.ActivityMain;
import com.jbslade.flashcards.language.german.model.Card;
import com.jbslade.flashcards.language.german.model.Deck;
import com.jbslade.flashcards.language.german.model.DeckElement;
import com.jbslade.flashcards.language.german.model.DeckTree;
import com.jbslade.flashcards.language.german.ui.CardView;
import com.jbslade.flashcards.language.german.ui.DeckElementView;
import com.jbslade.flashcards.language.german.ui.DeckView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Justin on 10/28/13.
 */
public class AdapterDeckNavigator extends BaseAdapter
{
    private int m_rowCount;
    private ArrayList<String> m_selectionPath;
    private DeckTree m_deckTree;
    private Handler m_mainUIHandler;

    public AdapterDeckNavigator(Handler mainUIHandler)
    {
        m_mainUIHandler = mainUIHandler;
        m_rowCount = 1;
        m_selectionPath = new ArrayList<String>();
    }

    public void setDeckTree(DeckTree deckTree)
    {
        this.m_deckTree = deckTree;
    }

    @Override
    public int getCount()
    {
        return m_rowCount;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater =
                (LayoutInflater) parent.getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
        View inflatedRowView = inflater.inflate(R.layout.list_main_navigation_adapter_row, null);
        View horizontalScrollView =
                inflatedRowView.findViewById(R.id.HorizontalScrollView_NavigationList_Row);
        LinearLayout linearLayout =
                (LinearLayout) horizontalScrollView
                        .findViewById(R.id.LinearLayout_NavigationList_ElementHolder);
        this.populateHorizontalLinearLayout(position, linearLayout);
        return inflatedRowView;
    }

    public void populateHorizontalLinearLayout(int rowNum, LinearLayout linearLayoutToScroll)
    {
        //Set up path
        ArrayList<String> path = new ArrayList<String>();
        for (int i = 0; i < rowNum; i++)
        {
            path.add(m_selectionPath.get(i));
        }
        Collection<DeckElement> elems = m_deckTree.findAndReturnDeckElements(path);
        for (DeckElement elem : elems)
        {
            View viewToAdd;
            if (elem instanceof Deck)
            {
                viewToAdd = new DeckView(linearLayoutToScroll.getContext(), (Deck) elem, rowNum);
            }
            else
            {
                viewToAdd = new CardView(linearLayoutToScroll.getContext(), (Card) elem, rowNum);
            }
            viewToAdd.setOnClickListener(getDeckElemViewClickListener());
            linearLayoutToScroll.addView(viewToAdd);
        }
    }

    private View.OnClickListener getDeckElemViewClickListener()
    {
        return new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (v instanceof DeckElementView)
                {
                    selectElement(((DeckElementView) v).getRowOfElemClicked(),
                            ((DeckElementView) v).getNameOfElemClicked(), v instanceof CardView);
                }
            }
        };
    }


    private void selectElement(int rowNum, String nameSelectedElem, boolean isCard)
    {
        ArrayList<String> newPath = new ArrayList<String>();
        for (int i = 0; i < rowNum; i++)
        {
            newPath.add(m_selectionPath.get(i));
        }
        newPath.add(nameSelectedElem);
        m_selectionPath = newPath;
        if (isCard)
        {
            sendUIMessage(getPathString(newPath), getCardNumber(), getCardNotes(nameSelectedElem));
            this.m_rowCount = rowNum + 1;
        }
        else
        {
            this.m_rowCount = rowNum + 2;
        }

        this.notifyDataSetChanged();

    }

    private String getPathString(ArrayList<String> path)
    {
        String formatPath = "";
        for (int i = 0; i < path.size(); i++)
        {
            formatPath += path.get(i) + (i != path.size() - 1 ? ">" : "");
        }
        return formatPath;
    }

    private void sendUIMessage(String path, int cardNum, String notes)
    {
        Bundle bundle = new Bundle();
        bundle.putShort(ActivityMain.KEY_MSG_TYPE, ActivityMain.MSG_TYPE_CARD_SELECTED);
        bundle.putString(ActivityMain.KEY_CARD_SELECTED_TITLE, path);
        bundle.putString(ActivityMain.KEY_CARD_SELECTED_NUMBER,
                "#" + String.valueOf(cardNum) + "/Total");
        bundle.putString(ActivityMain.KEY_CARD_SELECTED_NOTES, notes);
        Message msg = new Message();
        msg.setData(bundle);
        m_mainUIHandler.sendMessage(msg);
    }

    public int getCardNumber()
    {
        return -1;
    }

    public String getCardNotes(String nameSelectedElem)
    {
        return "These are card notes for Card: " + nameSelectedElem;
    }
}

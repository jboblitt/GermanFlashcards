package com.jbslade.flashcards.language.german.interfaces;

import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Justin on 10/9/13.
 */
public interface ListBasicAdapterControllerInterface<T>
{
    public boolean addElemsToList(ArrayList<T> elems);

    public int getCount();

    public T getItem(int position);

    public String getHeader1Text(int position);

    public String getHeader2aText(int position);

    public String getHeader2bText(int position);

    public void populateIcon(ImageView icon);
}

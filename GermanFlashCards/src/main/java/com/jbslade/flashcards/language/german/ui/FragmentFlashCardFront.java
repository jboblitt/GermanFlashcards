package com.jbslade.flashcards.language.german.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jbslade.flashcards.language.german.R;

/**
 * Created by Justin on 9/5/13.
 */
public class FragmentFlashCardFront extends FragmentFlashCard
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.flash_card_front, container, false);
    }
}

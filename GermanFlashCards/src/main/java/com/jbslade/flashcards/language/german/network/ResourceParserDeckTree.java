package com.jbslade.flashcards.language.german.network;

import com.jbslade.flashcards.language.german.debug.MyLogger;
import com.jbslade.flashcards.language.german.model.DeckTree;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Justin on 10/29/13.
 */
public class ResourceParserDeckTree extends ResourceParser<String>
{
    @Override
    public ArrayList<String> getResourceParsedElems(InputStream is)
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String read;

        try
        {
            while ((read = br.readLine()) != null)
            {
                sb.append(read);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        ArrayList<String> deckTrees = new ArrayList<String>();
        deckTrees.add(sb.toString());
        return deckTrees;
    }
}

package com.jbslade.flashcards.language.german.network;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Justin on 10/11/13.  As long as you can take an input stream and return an
 * ArrayList of elements, you've met the criteria of being a Parser.
 */
public abstract class ResourceParser<T>
{
    public abstract ArrayList<T> getResourceParsedElems(InputStream is);
}

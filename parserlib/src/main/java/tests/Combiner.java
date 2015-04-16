package tests;

import contentclasses.CategoryTree;
import contentclasses.Event;
import contentclasses.SportTree;
import tests.interfaceTest.ICombiner;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by retor on 15.04.2015.
 */
public class Combiner implements ICombiner {
    @Override
    public HashMap<String, SportTree> combineSports(ArrayList<SportTree>... sports) {
        return null;
    }

    @Override
    public HashMap<String, CategoryTree> combineCategories(ArrayList<CategoryTree>... categories) {
        return null;
    }

    @Override
    public HashMap<String, ArrayList<Event>> combineEvents(ArrayList<Event>... events) {
        return null;
    }
}

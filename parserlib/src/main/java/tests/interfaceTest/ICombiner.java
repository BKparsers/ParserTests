package tests.interfaceTest;

import contentclasses.CategoryTree;
import contentclasses.Event;
import contentclasses.SportTree;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by retor on 15.04.2015.
 */
public interface ICombiner {

    HashMap<String, SportTree> combineSports(ArrayList<SportTree>... sports);

    HashMap<String, CategoryTree> combineCategories(ArrayList<CategoryTree>... categories);

    HashMap<String, ArrayList<Event>> combineEvents(ArrayList<Event>... events);

}

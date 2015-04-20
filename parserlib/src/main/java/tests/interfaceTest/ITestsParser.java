package tests.interfaceTest;

import contentclasses.CategoryTree;
import contentclasses.Event;
import contentclasses.SportTree;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by retor on 06.04.2015.
 * @author retor
 *
 * Interface to parse data from loader
 * T is what you put in request(json, String, Element..... etc)
 * T1 is what be returned after parsing
 *
 * This interface uing in loaders to parse loaded data;
 */
public interface ITestsParser<T, T1> {


    /**
     * This method is first in work.
     * Here we taking main Json request object and do main work or saving
     * */
    T parseInput(JSONObject json) throws JSONException, NullPointerException;

    T1 findSports(T root);

    T1 findCategories(T sport);

    T1 findEvents(T category);

    SportTree parseSport(T response);

    CategoryTree parseCategory(T category);

    Event parseEvent(T event);

    ArrayList<String> getEventUrls(T category);

    String getBaseUrl();

    /**
     * This optional functions in latest versions be @deprecated
     * */
    int getCount();

    String getUpdateTime();
}

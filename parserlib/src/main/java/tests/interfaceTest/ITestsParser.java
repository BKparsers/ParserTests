package tests.interfaceTest;

import contentclasses.CategoryTree;
import contentclasses.Event;
import contentclasses.SportTree;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by retor on 06.04.2015.
 */
public interface ITestsParser<T, T1> {

    T parseInput(JSONObject json) throws JSONException, NullPointerException;

    T1 findSports(T root);

    T1 findCategories(T sport);

    T1 findEvents(T category);

    SportTree parseSport(T response);

    CategoryTree parseCategory(T category);

    Event parseEvent(T event);

    ArrayList<String> getEventUrls(T category);

    String getBaseUrl();

    int getCount();

    String getUpdateTime();
}

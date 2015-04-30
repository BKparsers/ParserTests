package tests.interfaceTest;

import contentclasses.CategoryTree;
import contentclasses.Event;
import contentclasses.SportTree;
import org.json.JSONObject;
import tests.exceptions.parserEx.ParsingException;

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
public interface ITestParser<T, T1> {
    /**
     * This method is first in work.
     * Here we taking main Json request object and do main work or saving
     * */
    T parseInput(JSONObject json) throws ParsingException;

    T1 findSports(T root) throws ParsingException;

    T1 findCategories(T sport) throws ParsingException;

    T1 findEvents(T category) throws ParsingException;

    SportTree parseSport(T response) throws ParsingException;

    CategoryTree parseCategory(T category) throws ParsingException;

    Event parseEvent(T event) throws ParsingException;

    ArrayList<String> getEventUrls(T category);

    String getBaseUrl();

    /**
     * This optional functions in latest versions be @deprecated
     * */
    int getCount();

    String getUpdateTime();
}

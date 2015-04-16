package interfaces;

import contentclasses.CategoryTree;
import contentclasses.Event;
import contentclasses.SportTree;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by retor on 23.03.2015.
 */
public interface IParser<T> {

    ArrayList<SportTree> createSports(JSONObject json, ILoader loader);

    SportTree parseSports(T response);

    CategoryTree parseCategory(T category);

    Event parseEvent(T event);

    String getBaseUrl();

    String getUpdandCount();

    int getCount();

    String getUpdateTime();

}

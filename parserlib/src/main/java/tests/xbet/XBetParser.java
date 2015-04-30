package tests.xbet;

import contentclasses.CategoryTree;
import contentclasses.Event;
import contentclasses.SportTree;
import org.json.JSONArray;
import org.json.JSONObject;
import tests.exceptions.parserEx.ParsingException;
import tests.interfaceTest.ITestParser;

import java.util.ArrayList;

/**
 * Created by retor on 22.04.2015.
 */
public class XBetParser implements ITestParser<JSONObject, JSONArray> {

    private String baseUrl = "https://xbetsport.com/LiveFeed/Get1x2?sportId=0&count=50&lng=ru&cfview=0";
    private String forEventsUrl = "https://xbetsport.com/LiveFeed/GetGame?id=";

    @Override
    public JSONArray findCategories(JSONObject sport) {
        return null;
    }

    @Override
    public JSONObject parseInput(JSONObject json) throws ParsingException{
        if (json!=null)
            return json;
        else
            throw new ParsingException("Null root JSON response");
    }

    @Override
    public JSONArray findSports(JSONObject root) throws ParsingException {
        return null;
    }

    @Override
    public JSONArray findEvents(JSONObject category) throws ParsingException {
        return null;
    }

    @Override
    public SportTree parseSport(JSONObject response)  throws ParsingException{
        return null;
    }

    @Override
    public CategoryTree parseCategory(JSONObject category) throws ParsingException {
        return null;
    }

    @Override
    public Event parseEvent(JSONObject event) throws ParsingException {
        JSONArray miscs = event.getJSONArray("miscs");
        JSONObject ev = event.getJSONObject("event");
        Event out = new Event();
        //TODO: parse event and miscs
        return out;
    }

    @Override
    public ArrayList<String> getEventUrls(JSONObject category) {
        ArrayList<String> out = new ArrayList<>();
        JSONArray arrayId = category.getJSONArray("events");
        for (int i = 0; i < arrayId.length(); i++) {
            out.add(forEventsUrl + String.valueOf(arrayId.get(i))+"&lng=ru&cfview=0"); //TODO: add full string to response
        }
        return out;
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public String getUpdateTime() {
        return null;
    }
}

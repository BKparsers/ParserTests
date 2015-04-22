package tests.xbet;

import contentclasses.CategoryTree;
import contentclasses.Event;
import contentclasses.SportTree;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tests.interfaceTest.ITestsParser;

import java.util.ArrayList;

/**
 * Created by retor on 22.04.2015.
 */
public class XBetParser implements ITestsParser<JSONObject, JSONArray> {

    private String baseUrl = "";
    private String forEventsUrl = "";

    @Override
    public JSONArray findCategories(JSONObject sport) {
        return null;
    }

    @Override
    public JSONObject parseInput(JSONObject json) throws JSONException, NullPointerException {
        return json;
    }

    @Override
    public JSONArray findSports(JSONObject root) {
        return null;
    }

    @Override
    public JSONArray findEvents(JSONObject category) {
        return null;
    }

    @Override
    public SportTree parseSport(JSONObject response) {
        return null;
    }

    @Override
    public CategoryTree parseCategory(JSONObject category) {
        return null;
    }

    @Override
    public Event parseEvent(JSONObject event) {
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
            out.add(forEventsUrl + String.valueOf(arrayId.get(i))); //TODO: add full string to response
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

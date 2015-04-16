package tests.bkfonbet;

import contentclasses.CategoryTree;
import contentclasses.Event;
import contentclasses.SportTree;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tests.interfaceTest.ITestsParser;

import java.util.ArrayList;

/**
 * Created by retor on 13.04.2015.
 */
public class Test2Parser implements ITestsParser<JSONObject, JSONArray> {

    private int count = 0;
    private String updTime, baseUrl = "https://live.bkfonbet.com/live/currentLine/ru/0";

    @Override
    public JSONObject parseInput(JSONObject json) throws JSONException, NullPointerException {
        return json;
    }

    @Override
    public JSONArray findSports(JSONObject root) {
        JSONArray out = new JSONArray();
        try {
            JSONArray tmpOut = root.getJSONArray("sports");

            for (int i = 0; i < tmpOut.length(); i++) {
                if (tmpOut.getJSONObject(i).optInt("parentId") == 0)
                    out.put(tmpOut.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return out;
    }

    @Override
    public JSONArray findCategories(JSONObject sport) {
        JSONArray out = new JSONArray();
        try {
            JSONArray tmpOut = sport.getJSONArray("sports");
            for (int i = 0; i < tmpOut.length(); i++) {
                if (tmpOut.getJSONObject(i).getString("kind").equals("segment"))
                    out.put(tmpOut.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return out;
    }

    @Override
    public JSONArray findEvents(JSONObject category) {
        JSONArray out = new JSONArray();
        try {
            out = category.getJSONArray("events");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return out;
    }

    @Override
    public SportTree parseSport(JSONObject response) {
        SportTree out = new SportTree();
        try {
            out.setId(response.getInt("id"));
            out.setName(response.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return out;
    }

    @Override
    public CategoryTree parseCategory(JSONObject category) {
        CategoryTree out = new CategoryTree();
        try {
            out.setId(category.getInt("id"));
            out.setName(category.getString("name"));
            out.setParentId(category.getInt("parentId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return out;
    }

    @Override
    public Event parseEvent(JSONObject event) {
        Event out = new Event();
        JSONObject tmp;
        if ((tmp = event.optJSONObject("level1")) != null) {
            try {
                out.setCommand1(tmp.getString("team1"));
                out.setCommand2(tmp.getString("team2"));
                out.setCategoryId(tmp.getInt("sportId"));
                out.setStartTime(tmp.getLong("startTime"));
                tmp = event.optJSONObject("miscs1");
                out.setEventStates(tmp.getInt("score1") + " " + tmp.getInt("score2") + " " + tmp.getString("comment"));
                out.addEventTotals(fromFactors("factors1", event));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if ((tmp = event.optJSONObject("level2")) != null) {
            try {
                out.setEventName(tmp.getString("name"));
                out.addEventForas(fromFactors("factors2", event));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return out;
    }

    private String fromFactors(String factorId, JSONObject event) throws JSONException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < event.getJSONArray(factorId).length(); i++) {
            JSONObject tmp = event.getJSONArray(factorId).getJSONObject(i);
            sb.append(tmp.getInt("f")).append(" ");
            if (tmp.optString("v") != "")
                sb.append(Float.valueOf(tmp.optString("v"))).append(" ");
            sb.append(tmp.optInt("p")).append(" ");
            if (tmp.optString("pt") != "")
                sb.append(Float.valueOf(tmp.optString("pt"))).append(" ");
        }
        return sb.toString();
    }

    @Override
    public ArrayList<String> getEventUrls(JSONObject category) {
        return null;
    }

    @Override
    public String getBaseUrl() {
        return this.baseUrl;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public String getUpdateTime() {
        return this.updTime;
    }
}

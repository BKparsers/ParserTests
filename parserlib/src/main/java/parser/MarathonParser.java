package parser;

import contentclasses.CategoryTree;
import contentclasses.Event;
import contentclasses.SportTree;
import interfaces.ILoader;
import interfaces.IParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import rx.functions.Action1;

import java.sql.Time;
import java.time.Instant;
import java.util.ArrayList;

/**
 * Created by retor on 03.04.2015.
 */
public class MarathonParser implements IParser<JSONObject> {

    private String baseUrl = "http://liveupdate.marathonbookmakers.com/su/liveupdate/";
    private int count;
    private long updated;
    ILoader loader;

    public MarathonParser() {
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public String getUpdateTime() {
        return Time.from(Instant.ofEpochMilli(updated)).toString();
    }

    @Override
    public String getUpdandCount() {
        if (count != 0 && updated != 0)
            return "Updated at: " + getUpdateTime() + "; Count: " + getCount();
        else
            return null;
    }

    @Override
    public ArrayList<SportTree> createSports(JSONObject json, ILoader loader) {
        this.loader = loader;
        Element body = null;
        try {
            body = Jsoup.parse(parseInput(json)).body();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return createSportTreeList(parseSportTrees(body));
    }

    @Override
    public SportTree parseSports(JSONObject response) {
        return null;
    }

    @Override
    public CategoryTree parseCategory(JSONObject category) {
        return null;
    }

    @Override
    public Event parseEvent(JSONObject event) {
        JSONArray rootTree = event.optJSONArray("modified");
        if (rootTree == null)
            rootTree = event.optJSONArray("available");
        Document doc = null;
        JSONObject eventTmp = null;
        Event ev = new Event();
        try {
            for (int i = 0; i < rootTree.length(); i++) {
                if (rootTree.getJSONObject(i).optString("type").equals("event")) {
                    eventTmp = rootTree.getJSONObject(i);
                }
            }
            ev.setCategoryId(eventTmp.optInt("treeId"));
            String html = eventTmp.optString("html");
            doc = Jsoup.parse(html);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ev.setCommand1(doc.select("div.live-today-member-name").first().text());
        ev.setCommand2(doc.select("div.live-today-member-name").last().text());
        ev.setEventStates(doc.body().select("div.cl-left").text() + doc.body().select("div.green bold nobr").text());
        return ev;
    }

    /**
     * Private section individual for every site parser*
     */

    private String parseInput(JSONObject object) throws JSONException, NullPointerException {
        String out = null;
        updated = object.getLong("updated");
        count = object.getInt("count");
        JSONArray modifies = object.optJSONArray("modified");
            /*Here need clear JSONarray delete menu and if need take categories---Only need (availables)*/
        if (modifies.length() > 0) {
            for (int i = 0; i < modifies.length(); i++) {
                if (modifies.getJSONObject(i).optString("type").equals("availables")) {
                    out = modifies.getJSONObject(i).getString("html").toString();
                }
            }
        }
        return out;
    }

    private Elements parseSportTrees(Element body) {
        Elements out = body.getElementsByAttribute("data-sport-treeid");
        return out;
    }

    private Elements parseCategoryTree(Element sportTree) {
        Elements out = sportTree.getElementsByAttribute("data-category-treeid");
        return out;
    }

    private Elements parseEventsAvailable(Element categryTree) {
        Elements out = categryTree.select("div.available-event");// data-event-treeid");
        return out;
    }

    private ArrayList<SportTree> createSportTreeList(Elements sportElements) {
        ArrayList<SportTree> out = new ArrayList<>();
        for (Element sport : sportElements) {
            int id = Integer.parseInt(sport.attr("data-sport-treeid"));
            String href = sport.select("a").attr("href");
            String name = sport.select("span").first().text();
            out.add(new SportTree(name, id, href).addItems(createCategoryTreesList(sport)));
        }
        return out;
    }

    private ArrayList<CategoryTree> createCategoryTreesList(Element sportTree) {
        ArrayList<CategoryTree> out = new ArrayList<>();
        for (Element category : parseCategoryTree(sportTree)) {
            int id = Integer.parseInt(category.attr("data-category-treeid"));
            String href = category.select("a").attr("href");
            String name = category.select("span").text();
            out.add(new CategoryTree(name, id, href).addItems(createEventsList(category)));
        }
        return out;
    }

    private ArrayList<Event> createEventsList(Element category) {
        ArrayList<Event> out = new ArrayList<>();
        ArrayList<String> eventUrls = new ArrayList<>();
        for (Element event : parseEventsAvailable(category)) {
            String tmpUrl = baseUrl + String.valueOf(event.attr("data-event-treeid"));
            if (!eventUrls.contains(tmpUrl))
                eventUrls.add(tmpUrl);
        }
        if (eventUrls.size() > 0) {
            loader.getEvents(eventUrls)
                    .subscribe(new Action1<ArrayList<Event>>() {
                        @Override
                        public void call(ArrayList<Event> events) {
                            out.addAll(events);
                        }
                    });
        }
        return out;
    }
}
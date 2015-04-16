package tests.marathon;

import contentclasses.CategoryTree;
import contentclasses.Event;
import contentclasses.SportTree;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tests.interfaceTest.ITestsParser;

import java.sql.Time;
import java.time.Instant;
import java.util.ArrayList;

/**
 * Created by retor on 06.04.2015.
 */
public class Test1parser implements ITestsParser<Element, Elements> {
    private String baseUrl = "https://liveupdate.marathonbookmakers.com/su/liveupdate/";
    private int count;
    private long updated;

    @Override
    public Element parseInput(JSONObject json) throws JSONException, NullPointerException {
        String out = null;
        updated = json.getLong("updated");
        count = json.getInt("count");
        JSONArray modifies = json.optJSONArray("modified");
            /*Here need clear JSONarray delete menu and if need take categories---Only need (availables)*/
        if (modifies.length() > 0) {
            for (int i = 0; i < modifies.length(); i++) {
                if (modifies.getJSONObject(i).optString("type").equals("availables")) {
                    out = modifies.getJSONObject(i).getString("html").toString();
                }
            }
        }
        return Jsoup.parse(out);
    }

    @Override
    public Elements findSports(Element root) {
        Elements out = root.getElementsByAttribute("data-sport-treeid");
        return out;
    }

    @Override
    public Elements findCategories(Element sport) {
        Elements out = sport.getElementsByAttribute("data-category-treeid");
        return out;
    }

    @Override
    public Elements findEvents(Element category) {
        Elements out = category.select("div.available-event");// data-event-treeid");
        return out;
    }

    @Override
    public SportTree parseSport(Element response) {
        int id = Integer.parseInt(response.attr("data-sport-treeid"));
        String href = response.select("a").attr("href");
        String name = response.select("span").first().text();
        return new SportTree(name, id, href);
    }

    @Override
    public CategoryTree parseCategory(Element category) {
        int id = Integer.parseInt(category.attr("data-category-treeid"));
        String href = category.select("a").attr("href");
        StringBuffer name = new StringBuffer();
        for (Element e : category.select("span.nowrap")) {
            name.append(e.text());
        }
        return new CategoryTree(name.toString(), id, href);
    }

    @Override
    public Event parseEvent(Element event) {
        Event ev = new Event();
        if (event.text().contains("removed")) {
            ev.setEventName("Event removed");
        } else {
            ev.setEventid(Integer.valueOf(event.select("input").attr("id")));
            ev.setCommand1(event.select("div.live-today-member-name").first().text());
            ev.setCommand2(event.select("div.live-today-member-name").last().text());
            ev.setEventStates(event.select("body").select("div.cl-left").text() + event.select("body").select("div.green bold nobr").text());

            getHandicaps(event, ev);
            getTotals(event, ev);
        }
        return ev;
    }

    private void getHandicaps(Element event, Event ev) {
        String t = "block" + ev.getEventid() + "type2";
        for (Element element : event.select("div").attr("id", t)) {
            for (Element e : element.select("td.price")) {
                String tmp = e.select("span").attr("data-selection-key");
                if (tmp.contains("Handicap") && tmp.contains("HB_A"))
                    ev.addForas1command(e.select("td").attr("data-sel").replace("&quot;", ""));
                if (tmp.contains("Handicap") && tmp.contains("HB_H"))
                    ev.addForas2command(e.select("td").attr("data-sel").replace("&quot;", ""));
            }
        }
    }

    private void getTotals(Element event, Event ev) {
        String t = "block" + ev.getEventid() + "type3";
        for (Element element : event.select("div").attr("id", t)) {
            for (Element e : element.select("td.price")) {
                String tmp = e.select("span").attr("data-selection-key");
                if (tmp.contains("Game_Total"))
                    ev.addEventTotals(e.select("td").attr("data-sel").replace("&quot;", ""));
                if (tmp.contains("Total") && tmp.contains("First_Team"))
                    ev.addTotals1command(e.select("td").attr("data-sel").replace("&quot;", ""));
                if (tmp.contains("Total") && tmp.contains("Second_Team"))
                    ev.addTotals2command(e.select("td").attr("data-sel").replace("&quot;", ""));
            }
        }

    }

    @Override
    public ArrayList<String> getEventUrls(Element category) {
        ArrayList<String> urls = new ArrayList<>();
        for (Element event : findEvents(category)) {
            String tmpUrl = baseUrl + String.valueOf(event.attr("data-event-treeid")) + "?callback&markets="
                    + String.valueOf(event.attr("data-event-treeid"));// + "&updated=" + System.currentTimeMillis();
//                    + "&oddsType=Decimal&siteStyle=MULTIMARKETS";
            if (!urls.contains(tmpUrl))
                urls.add(tmpUrl);
        }
        return urls;
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public String getUpdateTime() {
        return Time.from(Instant.ofEpochMilli(updated)).toString();
    }
}

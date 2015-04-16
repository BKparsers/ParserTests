package tests.bkfonbet;

import contentclasses.CategoryTree;
import contentclasses.Event;
import contentclasses.SportTree;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Observable;
import rx.Subscriber;
import tests.interfaceTest.ITestLoader;
import tests.interfaceTest.ITestsParser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by retor on 13.04.2015.
 */
public class Test2Loader implements ITestLoader {
    private ITestsParser<JSONObject, JSONArray> parser;
    private JSONObject rootobject;

    public Test2Loader(ITestsParser<JSONObject, JSONArray> parser) {
        this.parser = parser;
    }

    @Override
    public Observable<ArrayList<SportTree>> getData() {

        return Observable.create(new Observable.OnSubscribe<ArrayList<SportTree>>() {
            ArrayList<SportTree> out = new ArrayList<>();
            @Override
            public void call(Subscriber<? super ArrayList<SportTree>> subscriber) {
                rootobject = null;
                try {
                    rootobject = readResponse(parser.getBaseUrl());
                } catch (IOException | JSONException e) {
                    subscriber.onError(e);
                }
                all(rootobject).subscribe(sportTrees -> out.addAll(sportTrees), subscriber::onError, () -> {
                    subscriber.onNext(out);
                    subscriber.onCompleted();
                }).unsubscribe();
            }
        });
    }

    private JSONObject readResponse(String url) throws IOException, JSONException {
        Request r = Request.Get(url);
        r.socketTimeout(3500);
        r.connectTimeout(500);
        String tmp = null;
        try {
            HttpResponse resp = r.execute().returnResponse();
            int respcode = resp.getStatusLine().getStatusCode();
            if (respcode == 200)
                tmp = EntityUtils.toString(resp.getEntity(), Charset.forName("UTF-8"));
            if (respcode == 404 || respcode == 401)
                readResponse(url);
        } finally {
            r.abort();
        }
        return new JSONObject(tmp);
    }

    @Override
    public String getParserClassName() {
        return parser.getClass().getName();
    }

    @Override
    public void setParser(ITestsParser parser) {
        this.parser = parser;
    }

    @Override
    public ITestsParser getParser() {
        return this.parser;
    }

    private Observable<ArrayList<SportTree>> all(JSONObject root) {
        return Observable.create(new Observable.OnSubscribe<ArrayList<SportTree>>() {
            @Override
            public void call(Subscriber<? super ArrayList<SportTree>> subscriber) {
                ArrayList<SportTree> out = new ArrayList<>();
                JSONArray spors = parser.findSports(root);
                JSONArray categories = parser.findCategories(root);
                JSONArray events = parser.findEvents(root);
                try {
                    out = createSports(spors);
                    fillMainArray(out, createCategories(categories), createEvents(events));
                } catch (JSONException e) {
                    subscriber.onError(e);
                }
                subscriber.onNext(out);
                subscriber.onCompleted();
            }
        });
    }

    private void fillMainArray(ArrayList<SportTree> sportTrees, ArrayList<CategoryTree> categoryTrees, ArrayList<Event> events) {
        for (SportTree sport : sportTrees) {
            for (CategoryTree category : categoryTrees) {
                if (category.getParentId() == sport.getId()) {
                    for (Event event : events) {
                        if (event.getCategoryId() == category.getId()) {
                            category.addItem(event);
                        }
                    }
                    if (!sport.getItems().contains(category))
                        sport.addItem(category);
                }
            }
        }
    }

    private ArrayList<SportTree> createSports(JSONArray array) throws JSONException {
        ArrayList<SportTree> out = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            out.add(parser.parseSport(array.getJSONObject(i)));
        }
        return out;
    }

    private ArrayList<CategoryTree> createCategories(JSONArray array) throws JSONException {
        ArrayList<CategoryTree> out = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            out.add(parser.parseCategory(array.getJSONObject(i)));
        }
        return out;
    }

    private ArrayList<Event> createEvents(JSONArray events) throws JSONException {
        ArrayList<Event> out = new ArrayList<>();
        ArrayList<JSONObject> l1 = new ArrayList<>();
        ArrayList<JSONObject> l2 = new ArrayList<>();
        for (int i = 0; i < events.length(); i++) {
            JSONObject tmp = events.getJSONObject(i);
            if (tmp.getInt("level") == 1) {
                l1.add(tmp);
            } else if (tmp.getInt("level") == 2) {
                l2.add(tmp);
            }
        }
        for (int i = 0; i < l1.size(); i++) {
            JSONObject parse = new JSONObject();
            int tmpid = l1.get(i).getInt("id");
            parse.put("level1", l1.get(i));
            parse.put("miscs1", getEventMiscs(tmpid));
            parse.put("factors1", getEventFactors(tmpid));
            for (int k = 0; k < l2.size(); k++) {
                if (tmpid == l2.get(k).getInt("parentId")) {
                    parse.put("level2", l2.get(k));
//                    parse.put("miscs2", getEventMiscs(l2.get(k).getInt("parentId")));
                    parse.put("factors2", getEventFactors(l2.get(k).getInt("parentId")));
                }
            }
            out.add(parser.parseEvent(parse));
        }
/*
        for (int i = 0; i < events.length(); i++) {
            JSONObject tmp = events.getJSONObject(i);
            JSONObject parse = new JSONObject();
            if (tmp.optInt("parentId") != 0) {
                parse.put("level2", tmp);
                for (int k = 0; k < events.length(); k++) {
                    if (events.getJSONObject(k).optInt("id") == tmp.getInt("parentId")) {
                        parse.put("level1", events.getJSONObject(k));
                        events.remove(k);
                    }
                }
                events.remove(i);
                tmpEvent = parser.parseEvent(parse);
            } else {
                parse.put("level1", tmp);
                tmpEvent = parser.parseEvent(parse);
            }
            //parseMiscs(tmpEvent, findEventMiscs());
            out.add(tmpEvent);
        }
*/
        return out;
    }

    private JSONArray findEventFactors() throws JSONException {
        JSONArray out = this.rootobject.optJSONArray("customFactors");
        return out;
    }

    private JSONArray getEventFactors(int id) throws JSONException {
        JSONArray tmp = findEventFactors();
        JSONArray out = new JSONArray();
        for (int i = 0; i < tmp.length(); i++) {
            if (tmp.getJSONObject(i).getInt("e") == id)
                out.put(tmp.getJSONObject(i));
        }
        return out;
    }

    private JSONArray findEventMiscs() throws JSONException {
        JSONArray out = this.rootobject.optJSONArray("eventMiscs");
        return out;
    }

    private JSONObject getEventMiscs(int id) throws JSONException {
        JSONArray miscs = findEventMiscs();
        JSONObject out = null;
        for (int i = 0; i < miscs.length(); i++) {
            JSONObject tmp = miscs.getJSONObject(i);
            if (tmp.getInt("id") == id) {
                out = tmp;
            }
        }
        return out;
    }
}

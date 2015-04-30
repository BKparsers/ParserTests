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
import tests.exceptions.loaderEx.LoadingException;
import tests.exceptions.parserEx.ParsingException;
import tests.interfaceTest.ITestLoader;
import tests.interfaceTest.ITestParser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by retor on 13.04.2015.
 */
public class Test2Loader implements ITestLoader {
    private ITestParser<JSONObject, JSONArray> parser;
    private JSONObject rootobject;

    public Test2Loader(ITestParser<JSONObject, JSONArray> parser) {
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
                } catch (LoadingException e) {
                    subscriber.onError(e);
                }
                all(rootobject).subscribe(sportTrees -> out.addAll(sportTrees), subscriber::onError, () -> {
                    subscriber.onNext(out);
                    subscriber.onCompleted();
                }).unsubscribe();
            }
        });
    }

    public JSONObject readResponse(String url) throws LoadingException {
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
        } catch (IOException e) {
            throw new LoadingException("Exception At loading", e);
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
    public ITestParser getParser() {
        return this.parser;
    }

    @Override
    public void setParser(ITestParser parser) {
        this.parser = parser;
    }

    private Observable<ArrayList<SportTree>> all(JSONObject root) {
        return Observable.create(new Observable.OnSubscribe<ArrayList<SportTree>>() {
            @Override
            public void call(Subscriber<? super ArrayList<SportTree>> subscriber) {
                ArrayList<SportTree> out = new ArrayList<>();
                JSONArray spors = null;
                try {
                    spors = parser.findSports(root);
                    JSONArray categories = parser.findCategories(root);
                    JSONArray events = parser.findEvents(root);
                    out = createSports(spors);
                    fillMainArray(out, createCategories(categories), createEvents(events));
                } catch (ParsingException e) {
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

    private ArrayList<SportTree> createSports(JSONArray array) throws ParsingException {
        ArrayList<SportTree> out = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            out.add(parser.parseSport(array.getJSONObject(i)));
        }
        return out;
    }

    private ArrayList<CategoryTree> createCategories(JSONArray array) throws ParsingException {
        ArrayList<CategoryTree> out = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            out.add(parser.parseCategory(array.getJSONObject(i)));
        }
        return out;
    }

    private ArrayList<Event> createEvents(JSONArray events) throws ParsingException {
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

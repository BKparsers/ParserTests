package tests;

import contentclasses.CategoryTree;
import contentclasses.Event;
import contentclasses.SportTree;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import rx.Observable;
import rx.Subscriber;
import tests.exceptions.ParsingException;
import tests.interfaceTest.ITestLoader;
import tests.interfaceTest.ITestParser;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by retor on 09.04.2015.
 */
public class RxTests implements ITestLoader {

    ITestParser<Element, Elements> parser;

    public RxTests(ITestParser parser) {
        this.parser = parser;
    }

    @Override
    public Observable<ArrayList<SportTree>> getData() {
        Observable<JSONObject> mainRequest = request(parser.getBaseUrl());
        return Observable.create(new Observable.OnSubscribe<ArrayList<SportTree>>() {
            ArrayList<SportTree> out = new ArrayList<>();

            @Override
            public void call(Subscriber<? super ArrayList<SportTree>> subscriber) {
                mainRequest.flatMap(object -> getSportTreeObservable(object).doOnError(subscriber::onError)).subscribe(out::add);
                subscriber.onNext(out);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public String getParserClassName() {
        return parser.getClass().getName();
    }

    @Override
    public void setParser(ITestParser parser) {
        this.parser = parser;
    }

    @Override
    public ITestParser getParser() {
        return this.parser;
    }

    private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private Observable<JSONObject> request(String s) {
        return Observable.create(new Observable.OnSubscribe<JSONObject>() {
            @Override
            public void call(Subscriber<? super JSONObject> subscriber) {
                JSONObject outJson = null;
                try {
                    outJson = readJsonFromUrl(s);
                } catch (IOException | JSONException e) {
                    subscriber.onError(e);
                }
                subscriber.onNext(outJson);
                subscriber.onCompleted();
            }
        }).retry(5).onErrorResumeNext(Observable.create(new Observable.OnSubscribe<JSONObject>() {
            @Override
            public void call(Subscriber<? super JSONObject> subscriber) {
                JSONObject outJson = null;
                try {
                    outJson = readJsonFromUrl(s);
                } catch (IOException | JSONException e1) {
                    subscriber.onError(e1);
                }
                subscriber.onNext(outJson);
                subscriber.onCompleted();
            }
        }));
    }

    private Observable<SportTree> getSportTreeObservable(final JSONObject object) {
        return Observable.create(new Observable.OnSubscribe<SportTree>() {
            @Override
            public void call(Subscriber<? super SportTree> subscriber) {
                Element root = null;
                try {
                    root = parser.parseInput(object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    for (Element sport : parser.findSports(root)) {
                        SportTree tree = parser.parseSport(sport);
                        for (Element category : parser.findCategories(sport)) {
                            CategoryTree cat = parser.parseCategory(category);
                            eventObservable(category).subscribe(cat::addItem);
                            tree.addItem(cat);
                        }
                        subscriber.onNext(tree);
                    }
                } catch (ParsingException e) {
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            }
        });
    }

    private Element findEvent(JSONObject object) throws JSONException, NullPointerException {
        JSONArray rootTree = object.optJSONArray("modified");
        if (rootTree == null)
            rootTree = object.optJSONArray("available");
        String html = null;
        JSONObject eventTmp = null;
        for (int i = 0; i < rootTree.length(); i++) {
            if (rootTree.getJSONObject(i).optString("type").equals("event")) {
                eventTmp = rootTree.getJSONObject(i);
            }
        }
        html = eventTmp.optString("html");
        return Jsoup.parse(html);
    }

    private Observable<Event> eventObservable(Element category) {
        return Observable.from(parser.getEventUrls(category)).map(s -> {
            final Event[] e = new Event[1];
            request(s).subscribe(object -> {
                try {
                    e[0] = parser.parseEvent(findEvent(object));
                } catch (JSONException | ParsingException e1) {
                    e1.printStackTrace();
                }
            });
            return e[0];
        });
    }
}

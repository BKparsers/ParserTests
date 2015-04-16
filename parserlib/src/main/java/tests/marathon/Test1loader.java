package tests.marathon;

import contentclasses.CategoryTree;
import contentclasses.Event;
import contentclasses.SportTree;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import rx.Observable;
import rx.Subscriber;
import tests.interfaceTest.ITestLoader;
import tests.interfaceTest.ITestsParser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by retor on 06.04.2015.
 */
public class Test1loader implements ITestLoader {

    ITestsParser<Element, Elements> parser;

    public Test1loader(ITestsParser<Element, Elements> parser) {
        this.parser = parser;
    }

    @Override
    public Observable<ArrayList<SportTree>> getData() {
        ArrayList<SportTree> out = new ArrayList<>();
        return Observable.create(new Observable.OnSubscribe<ArrayList<SportTree>>() {
            @Override
            public void call(Subscriber<? super ArrayList<SportTree>> subscriber) {
                Element root = null;
                try {
                    root = parser.parseInput(readResponse(parser.getBaseUrl()));
                } catch (JSONException | IOException e) {
                    subscriber.onError(e);
                }
                Observable.from(parser.findSports(root))
                        .forEach(element -> {
                            SportTree tree = parser.parseSport(element);
                            Observable.from(parser.findCategories(element)).forEach(element1 -> {
                                CategoryTree category = parser.parseCategory(element1);
                                getEventList(parser.getEventUrls(element1))
                                        .subscribe(category::addItem, subscriber::onError, () -> tree.addItem(category)).unsubscribe();
                            });
                            out.add(tree);
                        }, subscriber::onError, () -> {
                            subscriber.onNext(out);
                            subscriber.onCompleted();
                        });
            }
        });
    }

    @Override
    public String getParserClassName() {
        return parser.getClass().getName();
    }

    @Override
    public ITestsParser getParser() {
        return this.parser;
    }

    @Override
    public void setParser(ITestsParser parser) {
        this.parser = parser;
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

    private Element findEvent(JSONObject object) throws JSONException {
        if (object.has("removed"))
            return Jsoup.parse("Event removed");
        JSONArray rootTree = object.optJSONArray("modified");
        if (rootTree == null)
            rootTree = object.optJSONArray("available");
        JSONObject eventTmp = null;
        for (int i = 0; i < rootTree.length(); i++) {
            if (rootTree.getJSONObject(i).optString("type").equals("event")) {
                eventTmp = rootTree.getJSONObject(i);
            }
        }
        String  html = null;
        if (eventTmp != null) {
            html = eventTmp.optString("html");
        }
        return Jsoup.parse(html);
    }

    private Observable<Event> getEventList(ArrayList<String> urls) {
        return Observable.from(urls).map(s -> {
            Event tmp = null;
            try {
                tmp = doEvent(s);
            } catch (Exception e) {

            }
            return tmp;
        });
    }

    private Event doEvent(String s) throws Exception {
        return parser.parseEvent(findEvent(readResponse(s)));
    }
}

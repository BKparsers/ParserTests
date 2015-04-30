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
import rx.functions.Func1;
import tests.exceptions.loaderEx.LoadingException;
import tests.exceptions.parserEx.ParsingException;
import tests.interfaceTest.ITestLoader;
import tests.interfaceTest.ITestParser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by retor on 06.04.2015.
 */
public class Test1loader implements ITestLoader {

    ITestParser<Element, Elements> parser;


    public Test1loader(ITestParser<Element, Elements> parser) {
        this.parser = parser;
    }

    @Override
    public Observable<ArrayList<SportTree>> getData(){
        ArrayList<SportTree> out = new ArrayList<>();
        return Observable.create(new Observable.OnSubscribe<ArrayList<SportTree>>() {
            @Override
            public void call(Subscriber<? super ArrayList<SportTree>> subscriber) {
                try {
                    Element root = parser.parseInput(readResponse(parser.getBaseUrl()));
                    Observable.from(parser.findSports(root))
                            .forEach(element -> {
                                try {
                                    SportTree tree = parser.parseSport(element);
                                    Observable.from(parser.findCategories(element)).forEach(element1 -> {
                                        try {
                                            CategoryTree category = parser.parseCategory(element1);
                                            getEventList(parser.getEventUrls(element1))
                                                    .subscribe(category::addItem, subscriber::onError, () -> tree.addItem(category)).unsubscribe();
                                        } catch (ParsingException | LoadingException e) {
                                            subscriber.onError(e);
                                        }
                                    });
                                    out.add(tree);
                                } catch (ParsingException e) {
                                    subscriber.onError(e);
                                }
                            }, subscriber::onError, () -> {
                                subscriber.onNext(out);
                                subscriber.onCompleted();
                            });

                } catch (ParsingException | LoadingException e) {
                    subscriber.onError(e);
                }
            }
        });
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

    public JSONObject readResponse(String url) throws LoadingException{
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
            throw new LoadingException("Can't read response" + Calendar.getInstance().toInstant().atZone(ZoneId.systemDefault()).toString(), e);
        } finally {
            r.abort();
        }
        if (tmp != null) {
            return new JSONObject(tmp);
        }else {
            throw new LoadingException("Response is null. Check request param");
        }
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

    private Observable<Event> getEventList(ArrayList<String> urls) throws LoadingException{
        return Observable.from(urls).map(new Func1<String, Event>() {
            Event event = null;

            @Override
            public Event call(String s) {
                try {
                    event = doEvent(s);
                } catch (LoadingException | ParsingException e) {
                    e.printStackTrace();
                }
                return event;
            }
        });
    }

    private Event doEvent(String s) throws LoadingException, ParsingException {
        return parser.parseEvent(findEvent(readResponse(s)));
    }
}

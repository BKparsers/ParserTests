package loader;

import contentclasses.Event;
import contentclasses.SportTree;
import interfaces.ILoader;
import interfaces.IParser;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Observable;
import rx.Subscriber;

import java.util.ArrayList;

/**
 * Created by retor on 03.04.2015.
 */
public class SimpleJsonLoader implements ILoader {

    private IParser<JSONObject> parser;

    public SimpleJsonLoader(IParser<JSONObject> parser) {
        this.parser = parser;
    }

    @Override
    public Observable<ArrayList<SportTree>> getData() {
        return Observable.create(new Observable.OnSubscribe<ArrayList<SportTree>>() {
            @Override
            public void call(Subscriber<? super ArrayList<SportTree>> subscriber) {
                getSports(parser.getBaseUrl())
                        .subscribe(sportTrees -> {
                            subscriber.onNext(sportTrees);
                        });
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<JSONObject> getResponse(String url) {
        return Observable.create(new Observable.OnSubscribe<JSONObject>() {
            @Override
            public void call(Subscriber<? super JSONObject> subscriber) {
                ((Runnable) () -> {
                    try {
                        subscriber.onNext(readJsonFromUrl(url));
                    } catch (Exception e) {
                        subscriber.onError(e.getCause());
                    }
                }).run();
                subscriber.onCompleted();
            }
        }).retry(3).onErrorReturn(throwable -> {
            JSONObject tmp = new JSONObject();
            try {
                tmp.put("message", throwable.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return tmp;
        });
    }

    @Override
    public Observable<ArrayList<SportTree>> getSports(String url) {
        ArrayList<SportTree> out = new ArrayList<>();
        return Observable.create(new Observable.OnSubscribe<ArrayList<SportTree>>() {
            @Override
            public void call(Subscriber<? super ArrayList<SportTree>> subscriber) {
                getResponse(url)
                        .subscribe(object -> {
                            out.addAll(parser.createSports(object, SimpleJsonLoader.this));
                        });
                subscriber.onNext(out);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<ArrayList<Event>> getEvents(ArrayList<String> urls) {
        ArrayList<Event> events = new ArrayList<>();
        return Observable.create(new Observable.OnSubscribe<ArrayList<Event>>() {
            @Override
            public void call(Subscriber<? super ArrayList<Event>> subscriber) {
                Observable.from(urls).forEach(s -> getResponse(s)
                        .subscribe(object -> {
                            events.add(parser.parseEvent(object));
                        }));
                subscriber.onNext(events);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public void setParser(IParser parser) {
        this.parser = parser;
    }

    @Override
    public String getParserClassName() {
        return parser.getClass().getName();
    }
}

/*
 * ******************************************************
 *                  * Copyright (C) 2015 retor  <retor@mail.ru>
 *                  *
 *                  * This file XBetLoader.java is part of  alfa 2.
 *                  *
 *                  * alfa 2 / parserlib can not be copied and/or distributed without the express
 *                  * permission
 * *****************************************************
 */

package tests.xbet;

import contentclasses.CategoryTree;
import contentclasses.Event;
import contentclasses.SportTree;
import org.json.JSONArray;
import org.json.JSONObject;
import rx.Observable;
import rx.Subscriber;
import tests.interfaceTest.ITestLoader;
import tests.interfaceTest.ITestsParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by retor on 22.04.2015.
 */
public class XBetLoader implements ITestLoader {

    private ITestsParser<JSONObject, JSONArray> parser;

    private JSONObject miscNames;

    public XBetLoader(ITestsParser<JSONObject, JSONArray> parser) {
        this.parser = parser;
        try {
            this.miscNames = readResponse(""); //TODO: URL to misc manes JS + add current date in format ddmmyyyy
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Observable<ArrayList<SportTree>> getData() {
        return Observable.create(new Observable.OnSubscribe<ArrayList<SportTree>>() {
            ArrayList<SportTree> sports = new ArrayList<>();
            ArrayList<CategoryTree> categoryes = new ArrayList<>();
            ArrayList<Event> events = new ArrayList<Event>();

            @Override
            public void call(Subscriber<? super ArrayList<SportTree>> subscriber) {
                getJsonRoot().subscribe(object -> {
                    sports.addAll(getting(object, 0));
                    categoryes.addAll(getting(object, 1));
                    loadEvents(parser.getEventUrls(new JSONObject().put("events", (ArrayList<Event>) getting(object, 2)))).forEach(events::add);
                });
                subscriber.onNext(combineAll(sports, categoryes, events));
                subscriber.onCompleted();
            }
        });
    }

    private ArrayList<SportTree> combineAll(ArrayList<SportTree> sports, ArrayList<CategoryTree> categoryes, ArrayList<Event> events) {
        fillCategoryItems(categoryes, events);
        sports.parallelStream().forEach(sportTree -> {
            int sId = sportTree.getId();
            categoryes
                    .stream()
                    .filter(categoryTree -> categoryTree.getParentId() == sId)
                    .forEach(sportTree::addItem);
        });
        return sports;
    }

    private void fillCategoryItems(ArrayList<CategoryTree> categoryTrees, ArrayList<Event> events) {
        categoryTrees.parallelStream().forEach(categoryTree -> {
            int cId = categoryTree.getId();
            events.stream().filter(event -> event.getEventid() == cId).forEach(categoryTree::addItem);
        });
    }

    /**
     * @return Observable with root Json Object
     */
    private Observable<JSONObject> getJsonRoot() {
        return Observable.create(new Observable.OnSubscribe<JSONObject>() {
            @Override
            public void call(Subscriber<? super JSONObject> subscriber) {
                try {
                    subscriber.onNext(parser.parseInput(Objects.requireNonNull(readResponse(parser.getBaseUrl()))));
                } catch (IOException e) {
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            }
        }).retry(3).onErrorResumeNext(getJsonRoot());
    }

    private Observable<Event> loadEvents(ArrayList<String> eventUrls) {
        return Observable.from(eventUrls).map(s -> {
            Event out = null;
            try {
                out = Objects.requireNonNull(parser.parseEvent(new JSONObject().put("event", readResponse(s)).put("miscs", miscNames)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return out;
        });
    }

    private ArrayList getting(JSONObject root, int param) {
        ArrayList out = null;
        JSONArray array;
        switch (param) {
            case 0:
                array = parser.findSports(root);
                out = new ArrayList<SportTree>();
                for (int i = 0; i < array.length(); i++) {
                    SportTree tmp = parser.parseSport(array.getJSONObject(i));
                    out.add(tmp);
                }
                break;
            case 1:
                array = parser.findCategories(root);
                out = new ArrayList<CategoryTree>();
                for (int i = 0; i < array.length(); i++) {
                    CategoryTree tmp = parser.parseCategory(array.getJSONObject(i));
                    out.add(tmp);
                }
                break;
            case 2:
                array = parser.findEvents(root);
                out = new ArrayList<Event>();
                for (int i = 0; i < array.length(); i++) {
                    int tmp = array.getJSONObject(i).optInt("id"); //TODO: valid key to ID
                    out.add(tmp);
                }
                break;
        }
        return Objects.requireNonNull(out);
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
}

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
import tests.exceptions.LoadingException;
import tests.exceptions.NullParserException;
import tests.exceptions.ParsingException;
import tests.interfaceTest.ITestLoader;
import tests.interfaceTest.ITestParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * Created by retor on 22.04.2015.
 */
public class XBetLoader implements ITestLoader {

    private ITestParser<JSONObject, JSONArray> parser;

    private JSONObject miscNames; 

    public XBetLoader(ITestParser<JSONObject, JSONArray> parser) {
        this.parser = parser;
    }

    @Override
    public Observable<ArrayList<SportTree>> getData() {
        return Observable.create(new Observable.OnSubscribe<ArrayList<SportTree>>() {
            ArrayList<SportTree> sports = new ArrayList<>();
            ArrayList<CategoryTree> categoryes = new ArrayList<>();
            ArrayList<Event> events = new ArrayList<>();
            @Override
            public void call(Subscriber<? super ArrayList<SportTree>> subscriber) {
                try {
                    miscNames = readResponse("https://xbetsport.com/js/betsNames_ru.js?f=" + (new SimpleDateFormat("ddMMyyyy").format(new Date())));
                } catch (LoadingException e) {
                    subscriber.onError(e);
                }
                getJsonRoot().subscribe(object -> {
                    try {
                        sports.addAll((ArrayList<SportTree>) getting(object, 0));
                        categoryes.addAll((ArrayList<CategoryTree>) getting(object, 1));
                        loadEvents(parser.getEventUrls(new JSONObject().put("events", (ArrayList<Event>) getting(object, 2)))).forEach(events::add);
                    } catch (ParsingException | LoadingException e) {
                        subscriber.onError(e);
                    }
                }, subscriber::onError, () -> {
                    subscriber.onNext(combineAll(sports, categoryes, events));
                    subscriber.onCompleted();
                });
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
                    subscriber.onNext(parser.parseInput(readResponse(parser.getBaseUrl())));
                } catch (LoadingException | ParsingException e) {
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            }
        });//.retry(3).onErrorResumeNext(getJsonRoot());
    }

    private Observable<Event> loadEvents(ArrayList<String> eventUrls) {
        return Observable.from(eventUrls).map(s -> {
            Event out = null;
            try {
                out = Objects.requireNonNull(parser.parseEvent(new JSONObject().put("event", readResponse(s)).put("miscs", miscNames)));
            } catch (LoadingException | ParsingException e) {
                e.printStackTrace();
            }
            return out;
        });
    }

    private ArrayList getting(JSONObject root, int param) throws ParsingException, LoadingException {
        ArrayList out = null;
        JSONArray array;
        switch (param) {
            case 0:
                array = parser.findSports(root);
                out = new ArrayList<SportTree>();
                if (array != null && array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        SportTree tmp = parser.parseSport(array.getJSONObject(i));
                        out.add(tmp);
                    }
                } else {
                    throw new LoadingException("Null array to parsing Sports");
                }
                break;
            case 1:
                array = parser.findCategories(root);
                out = new ArrayList<CategoryTree>();
                if (array != null && array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        CategoryTree tmp = parser.parseCategory(array.getJSONObject(i));
                        out.add(tmp);
                    }
                } else {
                    throw new LoadingException("Null array to parsing Caterories");
                }
                break;
            case 2:
                array = parser.findEvents(root);
                out = new ArrayList<Event>();
                if (array != null && array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        int tmp = array.getJSONObject(i).optInt("id"); //TODO: valid key to ID
                        out.add(tmp);
                    }
                } else {
                    throw new LoadingException("Null array to parsing Events");
                }
                break;
        }
        return Objects.requireNonNull(out);
    }

    @Override
    public String getParserClassName() {
        if (this.parser != null)
            return parser.getClass().getName();
        else
            return "No parser found";
    }

    @Override
    public ITestParser getParser() throws NullParserException {
        if (this.parser != null)
            return this.parser;
        else
            throw new NullParserException(getClass().getName() + " " + "This loader can't find parser. Please set parser for first");
    }

    @Override
    public void setParser(ITestParser parser) {
        this.parser = parser;
    }
}
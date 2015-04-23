/*
 * ******************************************************
 *                  * Copyright (C) 2015 retor  <retor@mail.ru>
 *                  *
 *                  * This file XBetLoaderTest.java is part of  alfa 2.
 *                  *
 *                  * alfa 2 / parserlib can not be copied and/or distributed without the express
 *                  * permission
 * *****************************************************
 */

package tests.xbet;

import contentclasses.CategoryTree;
import contentclasses.Event;
import contentclasses.SportTree;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import tests.interfaceTest.ITestsParser;

import java.util.ArrayList;

/**
 * Created by retor on 23.04.2015.
 */
public class XBetLoaderTest {
    ITestsParser parser = new ITestsParser() {
        @Override
        public Object parseInput(JSONObject json) throws JSONException, NullPointerException {
            return null;
        }

        @Override
        public Object findSports(Object root) {
            return null;
        }

        @Override
        public Object findCategories(Object sport) {
            return null;
        }

        @Override
        public Object findEvents(Object category) {
            return null;
        }

        @Override
        public SportTree parseSport(Object response) {
            return null;
        }

        @Override
        public CategoryTree parseCategory(Object category) {
            return null;
        }

        @Override
        public Event parseEvent(Object event) {
            return null;
        }

        @Override
        public ArrayList<String> getEventUrls(Object category) {
            return null;
        }

        @Override
        public String getBaseUrl() {
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public String getUpdateTime() {
            return null;
        }
    };
    XBetLoader loader = new XBetLoader(parser);

    @Test
    public void testGetData() throws Exception {

    }

    @Test
    public void testGetParserClassName() throws Exception {
        Assert.assertNotNull(loader.getParserClassName());
        Assert.assertEquals(loader.getParserClassName(), parser.getClass().getName());
    }

    @Test
    public void testGetParser() throws Exception {
        Assert.assertEquals(loader.getParser(), parser);
    }

    @Test
    public void testSetParser() throws Exception {
        ITestsParser pt = new ITestsParser() {
            @Override
            public Object parseInput(JSONObject json) throws JSONException, NullPointerException {
                return null;
            }

            @Override
            public Object findSports(Object root) {
                return null;
            }

            @Override
            public Object findCategories(Object sport) {
                return null;
            }

            @Override
            public Object findEvents(Object category) {
                return null;
            }

            @Override
            public SportTree parseSport(Object response) {
                return null;
            }

            @Override
            public CategoryTree parseCategory(Object category) {
                return null;
            }

            @Override
            public Event parseEvent(Object event) {
                return null;
            }

            @Override
            public ArrayList<String> getEventUrls(Object category) {
                return null;
            }

            @Override
            public String getBaseUrl() {
                return null;
            }

            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public String getUpdateTime() {
                return null;
            }
        };
        loader.setParser(pt);
        Assert.assertEquals(loader.getParser(), pt);
    }
}
/*
 * ******************************************************
 *                  * Copyright (C) 2015 retor  <retor@mail.ru>
 *                  *
 *                  * This file ParsersFabric.java is part of  alfa 2.
 *                  *
 *                  * alfa 2 / parserlib can not be copied and/or distributed without the express
 *                  * permission
 * *****************************************************
 */

package tests.fabrics;

import tests.bkfonbet.Test2Parser;
import tests.exceptions.ParserNotFoundException;
import tests.interfaceTest.ITestParser;
import tests.marathon.Test1parser;
import tests.xbet.XBetParser;

/**
 * Created by retor on 28.04.2015.
 */
public class ParsersFabric {
    public static ITestParser getParser(int what) throws ParserNotFoundException {
        switch (what){
            case Constants.MARATHON:return new Test1parser();
            case Constants.BKFONBET:return new Test2Parser();
            case Constants.XBET:return new XBetParser();
        }
        throw new ParserNotFoundException("Can't find your loader" + what);
    }


}

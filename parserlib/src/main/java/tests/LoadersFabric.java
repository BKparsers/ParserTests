/*
 * ******************************************************
 *                  * Copyright (C) 2015 retor  <retor@mail.ru>
 *                  *
 *                  * This file LoadersFabric.java is part of  alfa 2.
 *                  *
 *                  * alfa 2 / parserlib can not be copied and/or distributed without the express
 *                  * permission
 * *****************************************************
 */

package tests;

import tests.bkfonbet.Test2Loader;
import tests.exceptions.LoaderNotFoundException;
import tests.interfaceTest.ITestLoader;
import tests.interfaceTest.ITestParser;
import tests.marathon.Test1loader;
import tests.xbet.XBetLoader;

/**
 * Created by retor on 28.04.2015.
 */
public class LoadersFabric {

    public static ITestLoader getLoader(int what, ITestParser parserIn) throws LoaderNotFoundException {
        switch (what){
            case Constants.MARATHON:return new Test1loader(parserIn);
            case Constants.BKFONBET:return new Test2Loader(parserIn);
            case Constants.XBET:return new XBetLoader(parserIn);
        }
        throw new LoaderNotFoundException("Can't find your loader" + what);
    }
}

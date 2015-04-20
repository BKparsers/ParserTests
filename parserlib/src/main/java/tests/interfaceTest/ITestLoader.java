package tests.interfaceTest;

import contentclasses.SportTree;
import rx.Observable;

import java.util.ArrayList;

/**
 * Created by retor on 06.04.2015.
 *
 *Interface used in worker to load information
 */
public interface ITestLoader {

    /**
     * @return Observable with full array with SportTrees
     * filled CategoryTrees and Events
     */
    Observable<ArrayList<SportTree>> getData();

    /***
     * @return String
     * name of parser or class what used in current loader
     * */
    String getParserClassName();

    /***
     * @return ITestsParser object what used in current loader
     * */
    ITestsParser getParser();

    /**
     * Set parser to work in current loader
     * */
    void setParser(ITestsParser parser);
}

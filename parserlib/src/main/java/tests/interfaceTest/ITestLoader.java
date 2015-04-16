package tests.interfaceTest;

import contentclasses.SportTree;
import rx.Observable;

import java.util.ArrayList;

/**
 * Created by retor on 06.04.2015.
 */
public interface ITestLoader {

    Observable<ArrayList<SportTree>> getData();

    String getParserClassName();

    void setParser(ITestsParser parser);

    ITestsParser getParser();
}

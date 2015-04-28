package tests.interfaceTest;

import contentclasses.SportTree;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import rx.Observable;
import tests.exceptions.LoadingException;
import tests.exceptions.NullParserException;

import java.io.IOException;
import java.nio.charset.Charset;
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
    ITestParser getParser() throws NullParserException;

    /**
     * Set parser to work in current loader
     * */
    void setParser(ITestParser parser);

    default JSONObject readResponse(String url) throws LoadingException {
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
            throw new LoadingException("Can't loading root response", e);
        } finally {
            r.abort();
        }
        if (tmp!=null) {
            int start = tmp.indexOf('{');
            return new JSONObject(tmp.substring(start));
        }else {
            throw new LoadingException("Loaded response is NULL");
        }
    }
}

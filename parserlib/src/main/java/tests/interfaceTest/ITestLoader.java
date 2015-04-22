package tests.interfaceTest;

import contentclasses.SportTree;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Observable;

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
    ITestsParser getParser();

    /**
     * Set parser to work in current loader
     * */
    void setParser(ITestsParser parser);

    default JSONObject readResponse(String url) throws IOException, JSONException {
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
        } finally {
            r.abort();
        }
        return new JSONObject(tmp);
    }
}

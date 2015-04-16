package future.ruparimatch;

import org.apache.http.client.fluent.Request;
import org.json.JSONException;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by retor on 15.04.2015.
 */
public class HttpGetter {
    private String url;

    public HttpGetter() {

    }

    public String readResponse(String url) throws IOException, JSONException {
        Request r = Request.Get(url);
        String tmp = null;
        try {
            tmp = r.execute().returnContent().asString(Charset.forName("CP1251"));
        } finally {
            r.abort();
        }
        return tmp;
    }
}

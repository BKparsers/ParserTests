package future.xbet;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
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
        r.addHeader("Cookie", "SESSION=pfhsusb6qo194sbor1u6304ld7; tzo=4; blocks=1%2C1%2C1%2C1%2C1%2C1%2C1%2C1; _gat=1; lng=ru; _ga=GA1.2.591206844.1429209893; _ym_visorc_22934032=b");
        r.addHeader("X-Requested-With", "XMLHttpRequest");
        r.addHeader("Host", "https://xbetsports.com/live/");
        r.addHeader("If-Modified-Since", "Sat, 1 Jan 2000 00:00:00 GMT");
        r.addHeader("Accept-Encoding", "gzip, deflate, sdch");
        r.addHeader("Cache-Control", "no-cache");
        r.socketTimeout(3500);
        r.connectTimeout(500);
        String tmp = null;
        HttpResponse resp = r.execute().returnResponse();
        int respcode = resp.getStatusLine().getStatusCode();
        if (respcode == 200)
            tmp = EntityUtils.toString(resp.getEntity(), Charset.forName("UTF-8"));
        if (respcode == 404 || respcode == 401)
            readResponse(url);
        return tmp;
    }
}

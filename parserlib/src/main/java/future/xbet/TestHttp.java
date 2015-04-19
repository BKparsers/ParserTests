package future.xbet;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by retor on 15.04.2015.
 */
public class TestHttp {
    private HttpGetter getter = new HttpGetter();

    public TestHttp() {
        String tmp = null;
        String tmp1 = null;
        String url = "https://xbetsport.com/LiveFeed/Get1x2?sportId=0&count=50&lng=ru&cfview=0";

        try {
            tmp = getter.readResponse(url);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        //tmp1 = getter.readResponse(createUrl("http://ru.live.bwin.com/V2GetEventData.aspx?lang=17&eid=4362585&cts=635646823853072080&diff=1&cs=75A900F4&mbo=0&n=1&r=")).toString();

        tmp.length();
        //tmp1.length();
    }

    private String createUrl(String baseUrl) {
        return baseUrl + System.currentTimeMillis();
    }

/*    private String eventUrl(String url, int id){
        http://ru.live.bwin.com/V2GetEventData.aspx?lang=17&eid=4362585&cts=635646823853072080&diff=1&cs=75A900F4&mbo=0&n=1&r=1429085606378
    }*/
}

package future.betcity;

import future.bwin.HttpGetter;
import org.json.JSONException;

import java.io.IOException;

/**
 * Created by retor on 15.04.2015.
 */
public class TestHttp {
    private future.bwin.HttpGetter getter = new HttpGetter();

    public TestHttp() {
        String tmp = null;
        String tmp1 = null;
        String url = createUrl("http://ru.live.bwin.com/V2GetLiveEventsWithMainbets.aspx?label=1&lang=17&cts=635646811420514961&cs=75A900F7&n=1&r=");
        try {
            tmp = getter.readResponse(url).toString();
            tmp1 = getter.readResponse(createUrl("http://ru.live.bwin.com/V2GetEventData.aspx?lang=17&eid=4362585&cts=635646823853072080&diff=1&cs=75A900F4&mbo=0&n=1&r=")).toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tmp.length();
        tmp1.length();
    }

    private String createUrl(String baseUrl) {
        return baseUrl + System.currentTimeMillis();
    }

/*    private String eventUrl(String url, int id){
        http://ru.live.bwin.com/V2GetEventData.aspx?lang=17&eid=4362585&cts=635646823853072080&diff=1&cs=75A900F4&mbo=0&n=1&r=1429085606378
    }*/
}

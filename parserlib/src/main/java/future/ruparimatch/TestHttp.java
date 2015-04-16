package future.ruparimatch;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by retor on 15.04.2015.
 */
public class TestHttp {
    private HttpGetter getter = new HttpGetter();

    public TestHttp() {
        String root = null;
        String event = null;
        //String url = createUrl("http://ru.live.bwin.com/V2GetLiveEventsWithMainbets.aspx?label=1&lang=17&cts=635646811420514961&cs=75A900F7&n=1&r=");
        try {
            root = getter.readResponse("http://ruparimatch.com/live_as.html?curs=0&curName=$&shed=0");
            event = getter.readResponse(createEventUrl("http://ruparimatch.com/live_ar.html?hl=", 10308415));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        root.length();
        event.length();
    }

    private String createUrl(String baseUrl) {
        return baseUrl + System.currentTimeMillis();
    }

    private String createEventUrl(String url, int id) {
        return url + id;
    }

/*    private String eventUrl(String url, int id){
        http://ru.live.bwin.com/V2GetEventData.aspx?lang=17&eid=4362585&cts=635646823853072080&diff=1&cs=75A900F4&mbo=0&n=1&r=1429085606378
    }*/
}

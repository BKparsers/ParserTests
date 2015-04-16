package interfaces;

import contentclasses.Event;
import contentclasses.SportTree;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Observable;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by retor on 23.03.2015.
 */
public interface ILoader {

    default JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    default String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    Observable<ArrayList<SportTree>> getData();

    Observable<JSONObject> getResponse(String url);

    Observable<ArrayList<SportTree>> getSports(String url);

    Observable<ArrayList<Event>> getEvents(ArrayList<String> urls);

    String getParserClassName();

    void setParser(IParser parser);
}

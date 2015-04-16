package future.bwin;

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
        r.addHeader("Cookie", "ASP.NET_SessionId=00vzgsu2anmzbwst2eknnjbt; usersettings=cid=ru-RU&fv=false&ns=false&vc=1&s=2&sst=2015-04-15T09:41:19&psst=0001-01-01T00:00:00; mmcore.tst=0.235; mmid=1033370301%7CBAAAAAocpkV20gsAAA%3D%3D; mmcore.pd=495627566%7CBAAAAAoBQhymRXbSC9wbtvsBAHPf4w9nRdJIAA4AAABd+wuRZkXSSAAAAAD/////AP//////////AAZEaXJlY3QB0gsBAAAAAAABAAAAAAD///////////////8AAAAAAAFF; mmcore.srv=ldnvwcgeu08; mm_pc=Language%3DRussian%26Affiliate%3DOther%26Product%3DOther%26Bonus%3DNo%2520Bonus; ADRUM_BT=R%3a0%7cclientRequestGUID%3a78f54c84-3e84-415a-a8a7-d96ac8894997%7cbtId%3a7569%7cbtERT%3a2");
        r.addHeader("X-Requested-With", "ShockwaveFlash/17.0.0.134");
        r.addHeader("Host", "ru.live.bwin.com");
        String tmp = null;
        try {
            tmp = r.execute().returnContent().asString(Charset.forName("UTF-8"));
        } finally {
            r.abort();
        }
        return tmp;
    }
}

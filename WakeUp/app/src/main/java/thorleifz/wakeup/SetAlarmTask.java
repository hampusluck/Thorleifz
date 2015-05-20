package thorleifz.wakeup;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by adammyren on 15-05-20.
 */
public class SetAlarmTask extends AsyncTask<String, Void, String> {

    private String result;
    private String accountName;
    private String groupId;
    private String time;
    private int status;
    private final String serverURL = "https://script.google.com/macros/s/AKfycbwZ_k3B0xfsgIG9AgpsTsqIBZx_UtmVXjUD1--msnD218XQkbSC/exec";

    public SetAlarmTask(String accountName, String groupId, String time, int status){
        this.accountName = accountName;
        this.groupId = groupId;
        this.time = time;
        this.status = status;
    }

    @Override
    protected String doInBackground(String... params) {

        HttpClient httpClient = new DefaultHttpClient();
        String serverURLandParams = serverURL +"?accountName="+ accountName +"&groupId="+ groupId
                + "&time=" + time + "&status=" + status;
        Log.d("setAlarm", serverURLandParams);

        HttpGet httpGet = new HttpGet(serverURLandParams);
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            result = EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("SetAlarm",result);
        return result;
    }
}


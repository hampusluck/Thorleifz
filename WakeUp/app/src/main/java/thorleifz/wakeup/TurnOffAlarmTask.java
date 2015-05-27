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
 * Created by adammyren on 15-05-27.
 */
// side thread that notifies the database that the alarm has been turned off
public class TurnOffAlarmTask extends AsyncTask<String, Void, String> {
    String result;
    private final String alarmOffServerURL = "https://script.google.com/macros/s/AKfycbyhW8nDaJx8pGsw8kSVxc0XukPvrtjEQrh-fYg2Xyyol2XbRrQ/exec";
    private String accountName;
    private String groupId;
    private int status;


    public TurnOffAlarmTask(String accountName, String groupId,int status){
        this.accountName = accountName;
        this.groupId = groupId;
        this.status = status;
    }

    @Override
    protected String doInBackground(String... params) {

        HttpClient httpClient = new DefaultHttpClient();
        String serverURLandParams = alarmOffServerURL +"?accountName="+ accountName +"&groupId="+ groupId
                + "&status=" + status;
        Log.d("turnOffAlarm", serverURLandParams);

        HttpGet httpGet = new HttpGet(serverURLandParams);
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            result = EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("turnOffAlarm",result);
        return result;
    }
}
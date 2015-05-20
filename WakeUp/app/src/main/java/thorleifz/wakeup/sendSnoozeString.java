package thorleifz.wakeup;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by rebeccaharkonen on 2015-05-20.
 */
public class sendSnoozeString extends Activity {

    private final String serverURL = "https://script.google.com/macros/s/AKfycbxvLBgw0pY2fml0SEh4MX51TUbT34MIojhXw7FaQWGOjEhy1OD1/exec";
    private String accountName;
    private String groupId;
    private String snoozeString;
    private EditText setSnoozeStringEditText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_snooze_string_screen);
        setSnoozeStringEditText = (EditText) findViewById(R.id.setSnoozeStringEditText);

        groupId = getIntent().getStringExtra("groupId");
        accountName = getIntent().getStringExtra("accountName");




    }



    public void setSnoozeString(View view){

        snoozeString = setSnoozeStringEditText.getText().toString();
        snoozeString = snoozeString.replaceAll(" ", "_").toLowerCase();

        Log.d("snoozeString", snoozeString);
        Toast.makeText(this, "Snooze message set!", Toast.LENGTH_LONG).show();

        SetSnoozeStringTask setSnoozeStringTask = new SetSnoozeStringTask();
        setSnoozeStringTask.execute();
        finish();
    }

    private class SetSnoozeStringTask extends AsyncTask<String, Void, String> {
        String result;

        @Override
        protected String doInBackground(String... params) {

            HttpClient httpClient = new DefaultHttpClient();
            String serverURLandParams = serverURL +"?accountName="+ accountName +"&groupId="+ groupId
                    + "&snoozeString=" + snoozeString;
            Log.d("snoozeString", serverURLandParams);

            HttpGet httpGet = new HttpGet(serverURLandParams);
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                result = EntityUtils.toString(httpResponse.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("snoozeString",result);
            return result;
        }


    }
}

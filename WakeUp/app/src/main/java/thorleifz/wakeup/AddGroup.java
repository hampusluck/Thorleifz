package thorleifz.wakeup;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by rebeccaharkonen on 2015-04-27.
 */
public class AddGroup extends ActionBarActivity {
    private EditText inputGroupID;
    private EditText inputPassword;
    private TextView joinInfo;
    private String groupID;
    private String password;
    private String accountName;
    private SharedPreferences settings;
    SharedPreferences.Editor editor;
    private final String serverURL = "https://script.google.com/macros/s/AKfycbwDjF9pUpetKHHlYNhWuYVOES-e1zz652pyJJBTCdUgc39l4baB/exec";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_screen);
        settings = getSharedPreferences("settings",0);
        accountName = settings.getString("accountName", null);
        inputGroupID = (EditText)findViewById(R.id.joinGroupID);
        inputPassword = (EditText)findViewById(R.id.joinPassword);
        joinInfo = (TextView)findViewById(R.id.joinInfo);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    public void joinButtonPressed(View v) throws ExecutionException, InterruptedException {
        groupID = inputGroupID.getText().toString();
        password = inputPassword.getText().toString();
        if( (!groupID.equals("")) && !password.equals("")) {
            Log.i("tag", "not empty");

            JoinTask joinTask = new JoinTask();
            String joinStatus = joinTask.execute().get();

            if(joinStatus.equals("Join successful")){
                //Add member to group();
                Log.i("tag","success");
                // Go to "group activity"
                //Intent theIntent = new Intent(this, "group activity".class);
                //startActivity(theIntent);
            }
            else{
                Log.i("tag","not success");

                joinInfo.setText(joinStatus);
            }
        }

    }
   /* private void Add member to group() {
        editor = settings.edit();
        editor.putString("accountName",accountName);
        editor.putString("password",password);
        editor.commit();
    }*/


    private class JoinTask extends AsyncTask<String, Void, String> {
        String s;
        //Runs when the AddUser i executed, sends an HttpGet to the Google Script containing the accountName and password
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            String serverURLandParams = serverURL +"?accountName="+ accountName +"&password="+ password +"&groupId=";
            HttpGet httpGet = new HttpGet(serverURLandParams);
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                s = EntityUtils.toString(httpResponse.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return s;
        }

    }

}

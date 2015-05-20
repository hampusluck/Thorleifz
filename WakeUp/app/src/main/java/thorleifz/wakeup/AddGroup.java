package thorleifz.wakeup;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * You reach this activity by clicking the "+" button on the Groups screen.
 * The purpose of it is to be able to join an existing Group or click a button
 * to get to another activity where you can create a new group.
 *
 * Created by rebeccaharkonen on 2015-04-27.
 */
public class AddGroup extends ActionBarActivity {
    private EditText inputGroupId;
    private EditText inputPassword;
    private TextView joinInfo;
    private String groupId;
    private String password;
    private String accountName;
    private SharedPreferences settings;
    private ProgressBar joinProgressBar;
    private final String serverURL = "https://script.google.com/macros/s/AKfycbwDjF9pUpetKHHlYNhWuYVOES-e1zz652pyJJBTCdUgc39l4baB/exec";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_screen);
        settings = getSharedPreferences("settings",0);
        accountName = settings.getString("accountName", null);
        inputGroupId = (EditText)findViewById(R.id.joinGroupId);
        inputPassword = (EditText)findViewById(R.id.joinPassword);
        joinProgressBar = (ProgressBar)findViewById(R.id.joinProgressBar);
        joinProgressBar.setVisibility(View.GONE);
        joinInfo = (TextView)findViewById(R.id.joinInfo);
    }
    //removes keyboard
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideKeyboard();
        return true;
    }

    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private boolean containsSpaces(String string){
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }

    //the functionality a button which directs you to the CreateNewGroup activity
    public void createGroupButtonPressed(View v){
        Intent theIntent = new Intent(this, CreateNewGroup.class);
        startActivity(theIntent);
    }

    //when the group ID and group password is filled in, the functionality behind button checks if
    // there is a matching group and if so, lets you join that group
    public void joinButtonPressed(View v) throws ExecutionException, InterruptedException {
        hideKeyboard();
        joinInfo.setText("");
        groupId = inputGroupId.getText().toString();
        password = inputPassword.getText().toString();
        if( (!groupId.equals("")) && !password.equals("")) {
            if(!containsSpaces(groupId) && !containsSpaces(password)) {


                joinProgressBar.setVisibility(View.VISIBLE);
                JoinTask joinTask = new JoinTask();
                joinTask.execute();
            }else{
                joinInfo.setText("Wrong password or group ID");
            }
        }

    }

    private class JoinTask extends AsyncTask<String, Void, String> {
        String s;
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();

            String serverURLandParams = serverURL +"?accountName="+ accountName +"&groupPassword="+ password +"&groupId="+groupId;

            HttpGet httpGet = new HttpGet(serverURLandParams);
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                s = EntityUtils.toString(httpResponse.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("Group joined!")){
                DownloadGroupsTask downloadGroupsTask = new DownloadGroupsTask(accountName, getApplicationContext()); //downloads the groups and starts the Groups activity
                downloadGroupsTask.execute();
            }
            else{
                joinProgressBar.setVisibility(View.GONE);
                joinInfo.setText("Wrong username or password");
            }
        }
    }

}

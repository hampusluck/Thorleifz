package thorleifz.wakeup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

/**
 * You get to this activity by pressing the "create new group" button in the AddGroup activity.
 * In this activity you can create a new group.
 */


public class CreateNewGroup extends ActionBarActivity {

    String serverURL = "https://script.google.com/macros/s/AKfycbyL917crfmq3rm3laLN2ebLZyC_VSXSlYhE8kwCkNeHyezAQQ4/exec";
    EditText inputGroupID;
    EditText inputGroupName;
    EditText inputGroupPassword1;
    EditText inputGroupPassword2;
    TextView groupIDInfo;
    TextView groupPasswordInfo;
    String GroupID;
    String GroupPassword;
    SharedPreferences settings;
    String accountName;
    String GroupName;
    ProgressBar createGroupProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_group_screen);
        inputGroupID = (EditText)findViewById(R.id.groupID);
        inputGroupName = (EditText)findViewById(R.id.groupName);
        inputGroupPassword1 = (EditText) findViewById(R.id.createGroupPassword1);
        inputGroupPassword2 = (EditText) findViewById(R.id.createGroupPassword2);
        //groupIDInfo = (TextView) findViewById(R.id.createGroupIDInfo);
        groupPasswordInfo = (TextView) findViewById(R.id.createGroupPasswordInfo);
        createGroupProgressBar = (ProgressBar) findViewById(R.id.createGroupProgressBar);
        createGroupProgressBar.setVisibility(View.GONE);
        settings = getSharedPreferences("settings",0);
        accountName = settings.getString("accountName",null);
    }

    //pressing this button creates a new group and adds you to it if you fill in the editable
    // text fields correctly
    public void createGroupConfirmButtonPressed(View v){
        groupPasswordInfo.setText("");
        String GroupPassword1 = inputGroupPassword1.getText().toString();
        String GroupPassword2 = inputGroupPassword2.getText().toString();
        GroupID = inputGroupID.getText().toString();
        GroupName = inputGroupName.getText().toString();
        if( (!GroupID.equals("")) && (!GroupPassword1.equals("")) && (!GroupPassword2.equals("")) && (!GroupName.equals(""))) {
            createGroupProgressBar.setVisibility(View.VISIBLE);
            // Test for matching passwords
            if (GroupPassword1.equals(GroupPassword2)) {
                GroupPassword = GroupPassword1;
                createGroupProgressBar.setVisibility(View.VISIBLE);
                CreateGroupTask createGroupTask = new CreateGroupTask();
                createGroupTask.execute();
            }
            else{
                groupPasswordInfo.setText("Passwords don't match");
            }
        }

    }


    //removes keyboard
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_new_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class CreateGroupTask extends AsyncTask<String, Void, String> {
        String result;
        //Runs when the AddUser i executed, sends an HttpGet to the Google Script containing the accountName and password
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            String serverURLandParams = serverURL +"?groupId="+ GroupID +"&groupPassword="+ GroupPassword + "&accountName=" + accountName + "&groupName=" + GroupName;
            HttpGet httpGet = new HttpGet(serverURLandParams);
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                result = EntityUtils.toString(httpResponse.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("tag", result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(result.equals("Group created.")) {
                DownloadGroupsTask downloadGroupsTask = new DownloadGroupsTask(accountName,getApplicationContext());
                downloadGroupsTask.execute();
            }
            else {
                createGroupProgressBar.setVisibility(View.GONE);
                groupPasswordInfo.setText("GroupsID occupied");
            }
        }
    }
}

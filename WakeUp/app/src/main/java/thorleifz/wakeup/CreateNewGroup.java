package thorleifz.wakeup;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * You get to this activity by pressing the "create new group" button in the JoinGroup activity.
 * In this activity you can create a new group.
 */


public class CreateNewGroup extends Activity {

    private String serverURL = "https://script.google.com/macros/s/AKfycbyL917crfmq3rm3laLN2ebLZyC_VSXSlYhE8kwCkNeHyezAQQ4/exec";

    private EditText inputGroupID;
    private EditText inputGroupName;
    private EditText inputGroupPassword1;
    private EditText inputGroupPassword2;
    private ProgressBar createGroupProgressBar;

    private String groupId;
    private String groupPassword;
    private String accountName;
    private String groupName;

    private SharedPreferences settings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_group_screen);
        inputGroupID = (EditText)findViewById(R.id.groupID);
        inputGroupName = (EditText)findViewById(R.id.groupName);
        inputGroupPassword1 = (EditText) findViewById(R.id.createGroupPassword1);
        inputGroupPassword2 = (EditText) findViewById(R.id.createGroupPassword2);
        createGroupProgressBar = (ProgressBar) findViewById(R.id.createGroupProgressBar);
        createGroupProgressBar.setVisibility(View.GONE);

        settings = getSharedPreferences("settings",0);
        accountName = settings.getString("accountName",null);
    }

    //pressing this button creates a new group and adds you to it if you fill in the editable
    // text fields correctly
    public void createGroupConfirmButtonPressed(View v){
        hideKeyboard();
        String GroupPassword1 = inputGroupPassword1.getText().toString();
        String GroupPassword2 = inputGroupPassword2.getText().toString();
        groupId = inputGroupID.getText().toString();
        groupName = inputGroupName.getText().toString();

        // Test that all fields are filled in
        if( (!groupId.equals("")) && (!GroupPassword1.equals("")) && (!GroupPassword2.equals("")) && (!groupName.equals(""))) {
            createGroupProgressBar.setVisibility(View.VISIBLE);

            // Test for spaces in groupId
            if(!containsSpaces(groupId)) {
                // Test for spaces in groupPassword1
                if(!containsSpaces(GroupPassword1)) {
                    // Test for spaces in groupName
                    if (!containsSpaces(groupName)) {
                        // Test for matching passwords
                        if (GroupPassword1.equals(GroupPassword2)) {
                            groupPassword = GroupPassword1;
                            createGroupProgressBar.setVisibility(View.VISIBLE);
                            CreateGroupTask createGroupTask = new CreateGroupTask();
                            createGroupTask.execute();
                        } else {

                            Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Group name cannot contain blank spaces", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Passwords cannot contain blank spaces", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Group ID cannot contain blank spaces", Toast.LENGTH_LONG).show();
            }
        }

    }

    // Method for testing whether a string contains spaces or not
    private boolean containsSpaces(String string){
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }

    //Runs whenever the user touches the screen
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideKeyboard();
        return true;
    }

    // Hides the keyboard
    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }


    //Runs when the AddUser i executed, sends an HttpGet to the Google Script containing the accountName and password
    private class CreateGroupTask extends AsyncTask<String, Void, String> {
        String result;

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            String serverURLandParams = serverURL +"?groupId="+ groupId +"&groupPassword="+ groupPassword + "&accountName=" + accountName + "&groupId=" + groupName;
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
        // Checks if the group was successfully created, if so, it starts DownloadGroupsTask
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(result.equals("Group created.")) {
                DownloadGroupsTask downloadGroupsTask = new DownloadGroupsTask(accountName,getApplicationContext());
                downloadGroupsTask.execute();
            }
            else {
                createGroupProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Group ID occupied", Toast.LENGTH_LONG).show();
            }
        }
    }
}

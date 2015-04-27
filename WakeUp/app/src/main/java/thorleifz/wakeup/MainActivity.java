package thorleifz.wakeup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class MainActivity extends ActionBarActivity {

    private SharedPreferences settings;
    SharedPreferences.Editor editor;
    String accountName;
    String serverURL = "https://script.google.com/macros/s/AKfycbxu0F2ua8I8iu5NheQ8F6uzTiju1MLjLk_29-HU3jfoSfrhkeT0/exec";
    String theGroups;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = getSharedPreferences("settings",0); //gets the instance where data is locally stored
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    //Starts the Login-activity
    public void goToLoginScreen(View v){
        Intent theIntent = new Intent(this, LogIn.class);
        startActivity(theIntent);
    }
    //Starts the SignUp-activity
    public void signUpButtonPressed(View v){

        Intent theIntent = new Intent(this, SignUp.class);
        startActivity(theIntent);
    }
    //Downloads all the groupnames that the local user is a member of
    public void groupButtonPressed(View v) throws ExecutionException, InterruptedException {

        accountName = settings.getString("accountName",null); //Gets the accountName for the logged in user from local memory
        if(accountName!=null){
            DownloadGroupsTask downloadGroupsTask = new DownloadGroupsTask(); // Creates a new Thread that downloads the group names
            theGroups = downloadGroupsTask.execute().get(); // executes the thread, invoking the doInBackground-method in that class
        }

    }
    //Starts the group activity and passes a single String containing all the group names
    public void startGroupActivity(){
        Intent theIntent = new Intent(this, Groups.class);
        theIntent.putExtra("groups",theGroups);
        startActivity(theIntent);
    }

    //private class that runs the back-end script in a seperate thread
    private class DownloadGroupsTask extends AsyncTask<String, Void, String> {
        String s;
        //The doInBackground-method is called when the object is executed
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            String serverURLandParams = serverURL +"?accountName="+ accountName; //creates a new String containing the scripts URL and the parameters
            HttpGet httpGet = new HttpGet(serverURLandParams);
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                s = EntityUtils.toString(httpResponse.getEntity()); // The returned String is saved
            } catch (IOException e) {
                e.printStackTrace();
            }
            return s;
        }
        //This method is automatically when doInBackground is complete, in this case starting starting the new activity by calling startGroupActivity-method
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            startGroupActivity();
        }
    }
}

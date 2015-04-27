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
        settings = getSharedPreferences("settings",0);
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

    public void goToLoginScreen(View v){
        Intent theIntent = new Intent(this, LogIn.class);
        startActivity(theIntent);
    }

    public void signUpButtonPressed(View v){

        Intent theIntent = new Intent(this, SignUp.class);
        startActivity(theIntent);
    }
    public void groupButtonPressed(View v) throws ExecutionException, InterruptedException {

        accountName = settings.getString("accountName",null);
        if(accountName!=null){
            DownloadGroupsTask downloadGroupsTask = new DownloadGroupsTask();
            theGroups = downloadGroupsTask.execute().get();
        }

    }

    public void startGroupActivity(){
        Intent theIntent = new Intent(this, Groups.class);
        theIntent.putExtra("groups",theGroups);
        startActivity(theIntent);
    }


    private class DownloadGroupsTask extends AsyncTask<String, Void, String> {
        String s;
        //Runs when the AddUser i executed, sends an HttpGet to the Google Script containing the accountName and password
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            String serverURLandParams = serverURL +"?accountName="+ accountName;
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
            startGroupActivity();
        }
    }
}

package thorleifz.wakeup;

import android.app.Activity;
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


public class MainActivity extends Activity {

    private SharedPreferences settings;
    SharedPreferences.Editor editor;
    String accountName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences("settings",0); //gets the instance where data is locally stored
        if(userLoggedIn()){
            accountName = settings.getString("accountName",null); //Gets the username of the logged in user
            DownloadGroupsTask downloadGroupsTask = new DownloadGroupsTask(accountName,this);
            downloadGroupsTask.execute(); //Downloads a list of groups and starts the Groups Activity
        }
        else{
            startLoginActivity();
        }
    }



    @Override
<<<<<<< HEAD
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true; //Rebeccas kommentar
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
=======
    protected void onResume() {
        super.onResume();
    }

    //checks if there is a logged in user
    private boolean userLoggedIn() {
        String userLoggedIn = settings.getString("accountName","no user logged in");
        if(userLoggedIn.equals("no user logged in"))
            return false;
        return true;
    }

>>>>>>> develop


    //Starts the Login-activity
    public void startLoginActivity(){
        Intent theIntent = new Intent(this, LogIn.class);
        startActivity(theIntent);
    }





}

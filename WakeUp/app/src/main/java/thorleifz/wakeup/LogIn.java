package thorleifz.wakeup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
 * Created by Jacob on 2015-04-22.
 */
public class LogIn extends ActionBarActivity {

    private EditText inputUsername;
    private EditText inputPassword;
    private TextView loginInfo;
    private TextView passwordInfo;
    private String accountName;
    private String password;
    private SharedPreferences settings;
    SharedPreferences.Editor editor;
    private final String severURL = "https://script.google.com/macros/s/AKfycbxPWTBnFC0SHZ3n3JZzHxhkDvvUwcdw2VtQI9_NpNIUYQzV6tw/exec";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        settings = getSharedPreferences("settings",0); //gets the instance where data is locally stored
        inputUsername = (EditText)findViewById(R.id.UsernameLogIn);
        inputPassword = (EditText)findViewById(R.id.PasswordLogIn);
        loginInfo = (TextView)findViewById(R.id.loginInfo);
    }

    public void loginButtonPressed(View v) throws ExecutionException, InterruptedException {
            loginInfo.setText("");
            accountName = inputUsername.getText().toString();
            password = inputPassword.getText().toString();
        if( (!accountName.equals("")) && (!password.equals(""))) {
            LoginTask loginTask = new LoginTask();
            loginTask.execute();

/*            if(loginStatus.equals("Login successful")){
                createLocalUser();
                loginInfo.setText("You are logged in");
                // Go to "group activity"
                //Intent theIntent = new Intent(this, "group activity".class);
                //startActivity(theIntent);
            }
            else{
                loginInfo.setText("Wrong Accountname or password");
            }*/
        }
    }
    //Saves the entered information in the local memory using SharedPreferences
    private void createLocalUser() {
        editor = settings.edit();
        editor.putString("accountName",accountName);
        editor.putString("password",password);
        editor.commit();
    }

    public void startSignUpActivity(View v){
        Log.i("tag","in startsignupmethod");
        Intent theIntent = new Intent(this, SignUp.class);
        startActivity(theIntent);
    }

    @Override
    public void onBackPressed(){} //Overriding this method makes it impossible to go back to mainActivity

    //private class that runs the back-end script in a seperate thread
    private class LoginTask extends AsyncTask<String, Void, String> {
        String s;
        //The doInBackground-method is called when the object is executed
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            String serverURLandParams = severURL +"?accountName="+ accountName +"&password="+ password;//creates a new String containing the scripts URL and the parameters
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
            if(s.equals("Login successful")){
                createLocalUser();
                DownloadGroupsTask downloadGroupsTask = new DownloadGroupsTask(accountName, getApplicationContext()); //downloads the groups and starts the Groups activity
                downloadGroupsTask.execute();
            }
            else{
                loginInfo.setText("Wrong username or password");
            }
        }
    }
}

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
import android.widget.Button;
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
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * In this activity you can log in to your account or click a button to get to an activity
 * where you can create a new account.
 *
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
    private SharedPreferences.Editor editor;
    private ProgressBar loginProgressBar;
    private final String severURL = "https://script.google.com/macros/s/AKfycbxPWTBnFC0SHZ3n3JZzHxhkDvvUwcdw2VtQI9_NpNIUYQzV6tw/exec";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        settings = getSharedPreferences("settings",0); //gets the instance where data is locally stored
        inputUsername = (EditText)findViewById(R.id.UsernameLogIn);
        inputPassword = (EditText)findViewById(R.id.PasswordLogIn);
        loginProgressBar = (ProgressBar)findViewById(R.id.loginProgressBar);
        loginProgressBar.setVisibility(View.GONE);
        loginInfo = (TextView)findViewById(R.id.loginInfo);
    }

    public void loginButtonPressed(View v) throws ExecutionException, InterruptedException {
            hideKeyboard();
            loginInfo.setText("");
            accountName = inputUsername.getText().toString();
            password = inputPassword.getText().toString();

        if( (!accountName.equals("")) && (!password.equals(""))) {

            if(!containsSpaces(accountName) && !containsSpaces(password)) {

                loginProgressBar.setVisibility(View.VISIBLE);
                LoginTask loginTask = new LoginTask();
                loginTask.execute();

            }
            else{
                loginInfo.setText("Login unsuccessful");
            }
/*            if(loginStatus.equals("Login successful")){
                createLocalUser();
                loginInfo.setText("You are logged in");
                // Go to "group activity"
                //Intent theIntent = new Intent(this, "group activity".class);
                //startActivity(theIntent);
            }
            */
        }
    }
    //Saves the entered information in the local memory using SharedPreferences
    private void createLocalUser() {
        editor = settings.edit();
        editor.putString("accountName",accountName);
        editor.putString("password",password);
        editor.commit();
    }
    private boolean containsSpaces(String string){
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }

    public void startSignUpActivity(View v){
        Log.i("tag","in startsignupmethod");
        Intent theIntent = new Intent(this, SignUp.class);
        startActivity(theIntent);
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
                loginProgressBar.setVisibility(View.GONE);
                loginInfo.setText("Wrong username or password");
            }
        }
    }
}

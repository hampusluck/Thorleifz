package thorleifz.wakeup;

import android.app.Activity;
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
 * You get to this activity by clicking the "create new user" button on the login screen.
 * Here you can create a new user account.
 *
 * Created by rebeccaharkonen on 2015-04-22.
 */
public class SignUp extends Activity {

    private EditText inputUsername;
    private EditText inputPassword1;
    private EditText inputPassword2;
    private ProgressBar signupProgressBar;
    private String accountName;
    private String password;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private final String serverURL = "https://script.google.com/macros/s/AKfycbzuhhatsk9csXCv0oBKZ1TbtJqnLGsqrpR2ymTQStcrDaEgsGmP/exec";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);
        settings = getSharedPreferences("settings",0);
        signupProgressBar = (ProgressBar)findViewById(R.id.signupProgressBar);
        inputUsername = (EditText) findViewById(R.id.signupUsername);
        inputPassword1 = (EditText) findViewById(R.id.signupPassword1);
        inputPassword2 = (EditText) findViewById(R.id.signupPassword2);
        signupProgressBar.setVisibility(View.GONE);
    }

    // Runs when the sign up button is pressed.
    public void signUpConfirmButtonPressed(View v) {

        accountName = inputUsername.getText().toString();
        String password1 = inputPassword1.getText().toString();
        String password2 = inputPassword2.getText().toString();

        // Control text editors filled out
        if( (!accountName.equals("")) && (!password1.equals("")) && (!password2.equals("")) ) {
            // Test if accountName contains spaces
            if (!containsSpaces(accountName)) {
                // Test if password1 contains spaces
                if (!containsSpaces(password1)) {

                    // Test for matching passwords
                    if (password1.equals(password2)) {
                        signupProgressBar.setVisibility(View.VISIBLE);
                        password = password1;
                        AddUserTask addUserTask = new AddUserTask(); //Create a new AsyncTask that adds the user to the database
                        addUserTask.execute();
                    }

                    else
                        Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_LONG).show();
                }

                else
                    Toast.makeText(getApplicationContext(), "Your password cannot contain blank spaces", Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(getApplicationContext(), "Your account name cannot contain blank spaces", Toast.LENGTH_LONG).show();
        }

    }

    //Runs whenever the user clicks on the screen
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideKeyboard();
        return true;
    }

    //removes keyboard
    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    // Method that checks if a given string contains spaces or not
    private boolean containsSpaces(String string){
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }

    // Method that saves a user to the local memory in a settings file
    private void createLocalUser() {
        editor = settings.edit();
        editor.putString("accountName",accountName);
        editor.putString("password",password);
        editor.commit();
    }

    // Seperate thread that created a new user in the database
    // Runs when the AddUser i executed, sends an HttpGet to the Google Script containing the accountName and password
    private class AddUserTask extends AsyncTask<String, Void, String> {
        String result;

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            String serverURLandParams = serverURL +"?username="+ accountName +"&password="+ password;
            HttpGet httpGet = new HttpGet(serverURLandParams);
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                result = EntityUtils.toString(httpResponse.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("tag",result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            signupProgressBar.setVisibility(View.GONE);
            Log.i("tag", result);
            if(result.equals("New User created")) {
                createLocalUser();
                Intent intent = new Intent(getApplicationContext(), Groups.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(), "username occupied", Toast.LENGTH_LONG).show();
            }
        }
    }
}

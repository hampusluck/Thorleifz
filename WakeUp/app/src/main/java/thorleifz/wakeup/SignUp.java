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
    private TextView passwordInfo;
    private String accountName;
    private String password;
    private SharedPreferences settings;
    SharedPreferences.Editor editor;
    private final String serverURL = "https://script.google.com/macros/s/AKfycbzuhhatsk9csXCv0oBKZ1TbtJqnLGsqrpR2ymTQStcrDaEgsGmP/exec";
    boolean found = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);
        settings = getSharedPreferences("settings",0);
        signupProgressBar = (ProgressBar)findViewById(R.id.signupProgressBar);
        inputUsername = (EditText) findViewById(R.id.signupUsername);
        inputPassword1 = (EditText) findViewById(R.id.signupPassword1);
        inputPassword2 = (EditText) findViewById(R.id.signupPassword2);
        passwordInfo = (TextView) findViewById(R.id.signUpPasswordInfo);
        signupProgressBar.setVisibility(View.GONE);
    }

    public void signUpConfirmButtonPressed(View v) {

        accountName = inputUsername.getText().toString();
        String password1 = inputPassword1.getText().toString();
        String password2 = inputPassword2.getText().toString();
        passwordInfo.setText("");

        // Control text editors filled out
        if( (!accountName.equals("")) && (!password1.equals("")) && (!password2.equals("")) ) {

            if (!containsSpaces(accountName)) {

                if (!containsSpaces(password1)) {


                    // Test for matching passwords
                    if (password1.equals(password2)) {
                        Log.d("PAST EQUALS", "HEJ");
                        signupProgressBar.setVisibility(View.VISIBLE);
                        password = password1;
                        AddUserTask addUserTask = new AddUserTask(); //Create a new AsyncTask that saves adds the user to the database
                        addUserTask.execute();
                    }

                    else
                        passwordInfo.setText("Passwords don't match");
                }

                else
                    passwordInfo.setText("Your password cannot contain blank spaces");
            }
            else
                passwordInfo.setText("Your account name cannot contain blank spaces");
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

    private boolean containsSpaces(String string){
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }


    private void createLocalUser() {
        editor = settings.edit();
        editor.putString("accountName",accountName);
        editor.putString("password",password);
        editor.commit();
    }

    private class AddUserTask extends AsyncTask<String, Void, String> {
        String result;
        //Runs when the AddUser i executed, sends an HttpGet to the Google Script containing the accountName and password
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
                passwordInfo.setText("username occupied");
            }
        }
    }
}

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
 * Created by rebeccaharkonen on 2015-04-22.
 */
public class SignUp extends ActionBarActivity {

    private EditText inputUsername;
    private EditText inputPassword1;
    private EditText inputPassword2;
    private Button signUpConfirmButton;
    private TextView passwordInfo;
    private String accountName;
    private String password;
    private SharedPreferences settings;
    SharedPreferences.Editor editor;
    private final String severURL = "https://script.google.com/macros/s/AKfycbzuhhatsk9csXCv0oBKZ1TbtJqnLGsqrpR2ymTQStcrDaEgsGmP/exec";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);
        settings = getSharedPreferences("settings",0);
        inputUsername = (EditText) findViewById(R.id.signupUsername);
        inputPassword1 = (EditText) findViewById(R.id.signupPassword1);
        inputPassword2 = (EditText) findViewById(R.id.signupPassword2);
        signUpConfirmButton = (Button) findViewById(R.id.signupConfirmButton);
        passwordInfo = (TextView) findViewById(R.id.signUpPasswordInfo);

    }

    public void signUpConfirmButtonPressed(View v) throws ExecutionException, InterruptedException {
        accountName = inputUsername.getText().toString();
        String password1 = inputPassword1.getText().toString();
        String password2 = inputPassword2.getText().toString();

        // Control text editors filled out
        if( (!accountName.equals("")) && (!password1.equals("")) && (!password2.equals("")) ) {

            // Test for matching passwords
            if (password1.equals(password2)) {
                password = password1;
                AddUserTask addUserTask = new AddUserTask(); //Create a new AsyncTask that saves adds the user to the database
                String theResult = addUserTask.execute().get();
                if(theResult.equals("user created")) {
                    createLocalUser();
                    // Go to "group activity"
                    //Intent theIntent = new Intent(this, "group activity".class);
                    //startActivity(theIntent);
                }
                passwordInfo.setText(theResult);
                // Test for unique accountName
                // If unique send information (accountName, password) to database



            }
            else {
                passwordInfo.setText("Password NOT OK");
            }
        }
    }

    private void createLocalUser() {
        editor = settings.edit();
        editor.putString("accountName",accountName);
        editor.putString("password",password);
        editor.commit();
    }

    private class AddUserTask extends AsyncTask<String, Void, String> {
        String s;
        //Runs when the AddUser i executed, sends an HttpGet to the Google Script containing the accountName and password
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            String serverURLandParams = severURL +"?username="+ accountName +"&password="+ password;
            HttpGet httpGet = new HttpGet(serverURLandParams);
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                s = EntityUtils.toString(httpResponse.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return s;
        }

    }
}

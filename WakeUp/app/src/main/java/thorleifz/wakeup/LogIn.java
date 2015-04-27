package thorleifz.wakeup;

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
        settings = getSharedPreferences("settings",0);
        inputUsername = (EditText)findViewById(R.id.UsernameLogIn);
        inputPassword = (EditText)findViewById(R.id.PasswordLogIn);
        loginInfo = (TextView)findViewById(R.id.loginInfo);
    }

    public void loginButtonPressed(View v) throws ExecutionException, InterruptedException {
            accountName = inputUsername.getText().toString();
            password = inputPassword.getText().toString();
        if( (!accountName.equals("")) && (!password.equals("")) && (!password.equals("")) ) {
            Log.i("tag","not empty");

            LoginTask loginTask = new LoginTask();
            String loginStatus = loginTask.execute().get();

            if(loginStatus.equals("Login successful")){
                createLocalUser();
                Log.i("tag","success");
                // Go to "group activity"
                //Intent theIntent = new Intent(this, "group activity".class);
                //startActivity(theIntent);
            }
            else{
                Log.i("tag","not success");

                loginInfo.setText(loginStatus);
            }
        }

    }

    private void createLocalUser() {
        editor = settings.edit();
        editor.putString("accountName",accountName);
        editor.putString("password",password);
        editor.commit();
    }


    private class LoginTask extends AsyncTask<String, Void, String> {
        String s;
        //Runs when the AddUser i executed, sends an HttpGet to the Google Script containing the accountName and password
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            String serverURLandParams = severURL +"?accountName="+ accountName +"&password="+ password;
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

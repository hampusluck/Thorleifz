package thorleifz.wakeup;

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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by rebeccaharkonen on 2015-04-22.
 */
public class SignUp extends ActionBarActivity {

    EditText inputUsername;
    EditText inputPassword1;
    EditText inputPassword2;
    Button signUpConfirmButton;
    TextView passwordInfo;
    String accountName;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);

        inputUsername = (EditText) findViewById(R.id.signupUsername);
        inputPassword1 = (EditText) findViewById(R.id.signupPassword1);
        inputPassword2 = (EditText) findViewById(R.id.signupPassword2);
        signUpConfirmButton = (Button) findViewById(R.id.signupConfirmButton);
        passwordInfo = (TextView) findViewById(R.id.signUpPasswordInfo);

    }

    public void signUpConfirmButtonPressed(View v){
        accountName = inputUsername.getText().toString();
        String password1 = inputPassword1.getText().toString();
        String password2 = inputPassword2.getText().toString();

        // Control text editors filled out
        if( (!accountName.equals("")) && (!password1.equals("")) && (!password2.equals("")) ) {

            // Test for matching passwords
            if (password1.equals(password2)) {
                password = password1;
                Log.i("theTag","We create addUser");
                AddUser addUser = new AddUser();
                addUser.execute();
                // Test for unique accountName
                // If unique send information (accountName, password) to database
                // Go to "group activity"
                //Intent theIntent = new Intent(this, "group activity".class);
                //startActivity(theIntent);

                passwordInfo.setText("Password OK");
            }
            else {
                passwordInfo.setText("Password NOT ok");
            }
        }
    }

    private class AddUser extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpPost = new HttpGet("https://script.google.com/macros/s/AKfycbzuhhatsk9csXCv0oBKZ1TbtJqnLGsqrpR2ymTQStcrDaEgsGmP/exec");
            httpPost.getParams().setParameter("accountName",accountName);
            httpPost.getParams().setParameter("password",password);
            httpPost.getParams().setParameter("approved","Unprocessed");
            try {
                HttpResponse httpResponse = httpClient.execute(httpPost);
                Log.i("theTag", httpResponse.getParams().getParameter("approved").toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}

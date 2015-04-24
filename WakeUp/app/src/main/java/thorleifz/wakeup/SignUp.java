package thorleifz.wakeup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by rebeccaharkonen on 2015-04-22.
 */
public class SignUp extends ActionBarActivity {

    EditText inputUsername;
    EditText inputPassword1;
    EditText inputPassword2;
    Button signUpConfirmButton;
    TextView passwordInfo;

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
        String username = inputUsername.getText().toString();
        String password1 = inputPassword1.getText().toString();
        String password2 = inputPassword2.getText().toString();

        // Control text editors filled out
        if( (!username.equals("")) && (!password1.equals("")) && (!password2.equals("")) ) {

            // Test for matching passwords
            if (password1.equals(password2)) {

                // Test for unique username
                // If unique send information (username, password) to database
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
}

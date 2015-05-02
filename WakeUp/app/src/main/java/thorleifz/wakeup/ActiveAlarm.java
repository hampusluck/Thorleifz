package thorleifz.wakeup;

import android.app.AlarmManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

/**
 * Created by rebeccaharkonen on 2015-05-02.
 */
public class ActiveAlarm extends ActionBarActivity {

    private Button offButton;
    private Button snoozeButton;
    private TextView textView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_alarm_screen);
        offButton = (Button) findViewById(R.id.activeAlarmOffButton);
        snoozeButton = (Button) findViewById(R.id.activeAlarmSnoozeButton);
        textView = (TextView) findViewById(R.id.activeAlarmTextView);



    }


}

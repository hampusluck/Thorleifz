package thorleifz.wakeup;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by rebeccaharkonen on 2015-05-02.
 */
public class SetAlarm extends ActionBarActivity{

    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TimePicker alarmTimePicker;
    private static SetAlarm inst;
    private Button setAlarmConfirmButton;
    private TextView alarmTextView;

    public static SetAlarm instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_alarm_screen);
        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);
        setAlarmConfirmButton = (Button) findViewById(R.id.setAlarmConfirmButton);
        alarmTextView = (TextView) findViewById(R.id.alarmTextView);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


    }

    public void alarmConfirmButtonPressed(View view) {


            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
            Intent myIntent = new Intent(SetAlarm.this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(SetAlarm.this, 0, myIntent, 0);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
            setAlarmText("Larm inställt kl " + calendar.HOUR_OF_DAY + ":" + calendar.MINUTE);


    }
    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }

}

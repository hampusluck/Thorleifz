package thorleifz.wakeup;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by rebeccaharkonen on 2015-05-02.
 */
public class SetAlarm extends ActionBarActivity{

    AlarmManager alarmManager;
    private TimePicker alarmTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_alarm_screen);
        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);
        alarmTimePicker.setIs24HourView(true);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

    }

    // This method is called by the button "Set Alarm".
    // It calls the method setAlarm() with the time from the time picker.
    public void setAlarmTime(View view) {

        Calendar alarmTime = new GregorianCalendar();
        alarmTime.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
        alarmTime.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

        // If the time picker is set to an earlier time than the current time, the alarm day changes to tomorrow
        if (alarmTime.getTimeInMillis() >= System.currentTimeMillis()){
            setAlarm(alarmTime);
        }
        else {
            alarmTime.add(Calendar.DAY_OF_YEAR, 1);
            setAlarm(alarmTime);
        }

    }

    // This method sets the alarm
    private void setAlarm(Calendar alarmTime) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
        Toast.makeText(this, "Alarm set " + alarmTime.get(Calendar.HOUR) + ":" + alarmTime.get(Calendar.MINUTE),
                Toast.LENGTH_LONG).show();

        finish();
    }

    // This method cancels the alarm
    public void cancelAlarm(View view) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm canceled", Toast.LENGTH_LONG).show();

    }

}

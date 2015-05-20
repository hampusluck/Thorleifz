package thorleifz.wakeup;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by rebeccaharkonen on 2015-05-02.
 */
public class SetAlarm extends ActionBarActivity{

    AlarmManager alarmManager;
    private TimePicker alarmTimePicker;
    private String time;
    private SharedPreferences settings;
    private String accountName;
    private String groupId;

    private int INACTIVE_ALARM = 0;
    private int ACTIVE_ALARM = 1;
    private int SNOOZE_ALARM = 2;
    private int status;

    private final String serverURL = "https://script.google.com/macros/s/AKfycbwZ_k3B0xfsgIG9AgpsTsqIBZx_UtmVXjUD1--msnD218XQkbSC/exec";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_alarm_screen);
        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);
        alarmTimePicker.setIs24HourView(true);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        settings = getSharedPreferences("settings", 0);
        accountName = settings.getString("accountName", null);
        groupId = getIntent().getStringExtra("groupId");
    }

    // This method is called by the button "Set Alarm".
    // It calls the method setAlarm() with the time from the time picker.
    public void setAlarmTime(View view) {

        Calendar alarmTime = new GregorianCalendar();
        alarmTime.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
        alarmTime.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
        alarmTime.set(Calendar.SECOND, 0);
        alarmTime.set(Calendar.MILLISECOND, 0);
        status = ACTIVE_ALARM;

        time = String.format("%02d%02d", alarmTimePicker.getCurrentHour(), alarmTimePicker.getCurrentMinute());

        // If the time picker is set to an earlier time than the current time, the alarm day changes to tomorrow
        if (alarmTime.getTimeInMillis() >= System.currentTimeMillis()){
            setAlarm(alarmTime);
        }
        else {
            alarmTime.add(Calendar.DAY_OF_YEAR, 1);
            setAlarm(alarmTime);
        }
        SharedPreferences.Editor editor = settings.edit();

        String key = "myTime" + groupId;
        editor.putString(key,time);

        editor.commit();
        finish();
    }

    // This method sets the alarm
    private void setAlarm(Calendar alarmTime) {
        Log.d("setAlarm", alarmTime.toString());
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("groupId", groupId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), groupId.hashCode(), intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
        Toast.makeText(this, "Alarm set ", Toast.LENGTH_LONG).show();

        SetAlarmTask setAlarmTask = new SetAlarmTask(accountName, groupId, time, status);
        setAlarmTask.execute();
    }

/*    private class SetAlarmTask extends AsyncTask<String, Void, String> {
        String result;

        @Override
        protected String doInBackground(String... params) {

            HttpClient httpClient = new DefaultHttpClient();
            String serverURLandParams = serverURL +"?accountName="+ accountName +"&groupId="+ groupId
                    + "&time=" + time + "&status=" + status;
            Log.d("setAlarm", serverURLandParams);

            HttpGet httpGet = new HttpGet(serverURLandParams);
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                result = EntityUtils.toString(httpResponse.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("SetAlarm",result);
            return result;
        }


    }*/

}

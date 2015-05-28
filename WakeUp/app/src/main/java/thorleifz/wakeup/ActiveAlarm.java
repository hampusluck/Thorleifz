package thorleifz.wakeup;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by rebeccaharkonen on 2015-05-02.
 */
public class ActiveAlarm extends Activity {

    private Ringtone ringtone;
    private Vibrator vibrator;
    private TextView snoozeStringView;
    private String snoozeString = "ERROR";


    private String time;
    private SharedPreferences settings;
    private String accountName;
    private String groupId;

    //variables that represent the alarm state
    private int INACTIVE_ALARM = 0;
    private int ACTIVE_ALARM = 1;
    private int SNOOZE_ALARM = 2;
    private int status;

    private final String setAlarmServerURL = "https://script.google.com/macros/s/AKfycbwZ_k3B0xfsgIG9AgpsTsqIBZx_UtmVXjUD1--msnD218XQkbSC/exec";
    private final String alarmOffServerURL = "https://script.google.com/macros/s/AKfycbyhW8nDaJx8pGsw8kSVxc0XukPvrtjEQrh-fYg2Xyyol2XbRrQ/exec";
    private final String getSnoozeStringURL = "https://script.google.com/macros/s/AKfycbwgzzbw-NXaxYfB8urQ0PM8rsKp2S3zgoTWG5LYHTRevHcIus0/exec";
    // A variable that indicates if the user has turned of the alarm.
    private boolean turnedOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_alarm_screen);

        snoozeStringView = (TextView) findViewById(R.id.activeAlarmTextView);
        turnedOff = false;
        settings = getSharedPreferences("settings", 0);
        accountName = settings.getString("accountName", null);
        groupId = getIntent().getStringExtra("groupId");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(this, alarmUri);

        getSnoozeString();

    }

    @Override
    protected void onResume(){
        super.onResume();

        // Create a pattern for alarm vibration
        long[] pattern = {0, 1000, 1000};

        // Start vibration and alarm tone
        vibrator.vibrate(pattern, 0);
        ringtone.play();
    }

    private void getSnoozeString(){
        //Starts the asynctask GetSnoozeStringTask
        GetSnoozeStringTask getSnoozeStringTask = new GetSnoozeStringTask();
        getSnoozeStringTask.execute();
    }

    //function that reformats spaces to underscores before sending to database
    private void setSnoozeTextView(){
        snoozeString = snoozeString.replaceAll("_", " ").toLowerCase();
        snoozeStringView.setText(snoozeString);
    }

    // This method is called when the user presses the button to snooze
    public void snoozeButtonPressed(View view){
        snooze();
        finish();
    }

    //This method is called when the user presses the button to turn of the alarm
    public void turnOffAlarmButtonPressed(View view){
        turnOffAlarm();
        setLocalAlarmInactive();
        finish();
    }

    //Sets the alarm in SharedPreferences to inactive
    private void setLocalAlarmInactive() {
        SharedPreferences.Editor editor = settings.edit();
        String AlarmActiveKey = "alarmActive" + groupId;
        editor.putBoolean(AlarmActiveKey,false);
        editor.commit();
    }

    // This method stops the alarm and generates a new one within the snoozing time
    private void snooze(){
        ringtone.stop();
        vibrator.cancel();

        // Snoozing time in minutes
        int snoozeTime = 1;

        status = SNOOZE_ALARM;
        Calendar alarmTime = new GregorianCalendar();
        alarmTime.add(Calendar.MINUTE, snoozeTime);
        time = String.format("%02d%02d", alarmTime.get(Calendar.HOUR_OF_DAY), alarmTime.get(Calendar.MINUTE));

        long newAlarmTime = System.currentTimeMillis() + (snoozeTime * 1000 * 10);
        setAlarm(newAlarmTime);

        SnoozeAlarmTask snoozeAlarmTask = new SnoozeAlarmTask();
        snoozeAlarmTask.execute();

    }
    //Sets a new alarm at a given time
    private void setAlarm(long time) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("groupId", groupId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }

    // This method stops the alarm
    private void turnOffAlarm(){
        turnedOff = true;
        status = INACTIVE_ALARM;

        TurnOffAlarmTask turnOffAlarmTask = new TurnOffAlarmTask(accountName,groupId,status);
        turnOffAlarmTask.execute();
    }

    // This method is called whenever the activity is not in the foreground
    // For example if the user presses "back" or "home" button or finish is called

    @Override
    protected void onStop(){
       // If the alarm has not been turned off yet, generate a snooze
        ringtone.stop();
        vibrator.cancel();
        super.onStop();
    }


    // side thread that notifies the database of a new snooze alarm
    private class SnoozeAlarmTask extends AsyncTask<String, Void, String> {
        String result;

        @Override
        protected String doInBackground(String... params) {

            HttpClient httpClient = new DefaultHttpClient();
            String serverURLandParams = setAlarmServerURL +"?accountName="+ accountName +"&groupId="+ groupId
                    + "&time=" + time + "&status=" + status;
            Log.d("SnoozeAlarm", serverURLandParams);

            HttpGet httpGet = new HttpGet(serverURLandParams);
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                result = EntityUtils.toString(httpResponse.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("SnoozeAlarm",result);
            return result;

        }


    }

    // side thread that downloads the snooze string from the database
    private class GetSnoozeStringTask extends AsyncTask<String, Void, String> {
        protected String result;

        @Override
        protected String doInBackground(String... params) {

            HttpClient httpClient = new DefaultHttpClient();

            String serverURLandParams = getSnoozeStringURL +"?accountName="+ accountName +"&groupId="+ groupId;

            HttpGet httpGet = new HttpGet(serverURLandParams);
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                result = EntityUtils.toString(httpResponse.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d("SnoozeAlarm",result);

            snoozeString = result.toString();
            Log.d("NU ÄR VI HÄR", snoozeString);

            return result;
        }

        @Override
        //sets the alarmtext to be equal to the string that was downloaded
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setSnoozeTextView();
        }
    }

}

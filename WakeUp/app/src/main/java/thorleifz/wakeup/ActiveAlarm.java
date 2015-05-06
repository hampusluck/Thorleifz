package thorleifz.wakeup;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

/**
 * Created by rebeccaharkonen on 2015-05-02.
 */
public class ActiveAlarm extends Activity {

    private Ringtone ringtone;
    private Vibrator vibrator;

    // A variable that indicates if the user has turned of the alarm.
    private boolean turnedOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_alarm_screen);
        turnedOff = false;

        // Flags that enable the activity to turn on the screen, dismiss the keyguard and show up when locked
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(this, alarmUri);
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

    // This method is called when the user presses the button to snooze
    public void snoozeButtonPressed(View view){
        snooze();
        finish();
    }

    //This method is called when the user presses the button to turn of the alarm
    public void turnOffAlarmButtonPressed(View view){
        turnOffAlarm();
        finish();
    }

    // This method stops the alarm and generates a new one within the snoozing time
    private void snooze(){
        ringtone.stop();
        vibrator.cancel();

        // Snoozing time in seconds
        int snoozeTime = 10;

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (snoozeTime * 1000), pendingIntent);
    }

    // This method stops the alarm
    private void turnOffAlarm(){
        turnedOff = true;
    }

    // This method is called whenever the activity is not in the foreground
    // For example if the user presses "back" or "home" button or finish is called
    @Override
    protected void onPause(){
        // If the alarm has not been turned off yet, generate a snooze
        if (turnedOff == false){
            snooze();
        }
        ringtone.stop();
        vibrator.cancel();
        super.onPause();
    }
}

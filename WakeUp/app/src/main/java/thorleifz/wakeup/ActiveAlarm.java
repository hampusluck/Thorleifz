package thorleifz.wakeup;

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
    private Ringtone ringtone;
    private Vibrator vibrator;
    private boolean turnedOff = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_alarm_screen);
        offButton = (Button) findViewById(R.id.activeAlarmOffButton);
        snoozeButton = (Button) findViewById(R.id.activeAlarmSnoozeButton);
        textView = (TextView) findViewById(R.id.activeAlarmTextView);

        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);


        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(this, alarmUri);

    }

    @Override
    protected void onResume(){
        super.onResume();
        // vibration
        long[] pattern = {1000, 1000};
        vibrator.vibrate(pattern, 1);

        ringtone.play();
    }



    public void snoozeButtonPressed(View view){

        snooze();
    }

    public void turnOffAlarmButtonPressed(View view){

        turnOffAlarm();
    }

    private void snooze(){
        ringtone.stop();
        vibrator.cancel();
        //skicka info till gruppmedlemmar

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // snooze is now 10 seconds
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent);

    }

    private void turnOffAlarm(){
        ringtone.stop();
        vibrator.cancel();
        turnedOff = true;
        //skicka info till gruppmedlemmar
    }

    //Ser till att ringandet slutar om skärmen försvinner
    @Override
    protected void onPause(){
        if (turnedOff == false){
            snooze();
        }
        ringtone.stop();
        vibrator.cancel();
        super.onPause();

    }
}

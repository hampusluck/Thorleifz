package thorleifz.wakeup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

/**
 * Created by rebeccaharkonen on 2015-05-02.
 */
public class AlarmReceiver extends BroadcastReceiver {

    // When receiving appending intent from AlarmManager this method is called.
    // The activity ActiveAlarm is started.
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, ActiveAlarm.class);
        i.putExtra("groupId", intent.getStringExtra("groupId"));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

}

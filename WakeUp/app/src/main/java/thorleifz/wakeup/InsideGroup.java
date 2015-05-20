package thorleifz.wakeup;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * When you click on a group you get to this activity where you can see all members of it.
 * You should also be able to set your own alarm and see other peoples alarm statuses.
 */
public class InsideGroup extends ActionBarActivity {

    ListView membersListView;

    SharedPreferences settings;
    MemberListItemAdapter memberListItemAdapter;
    String groupId;
    String accountName;
    String membersString;
    Button AlarmActiveButton;
    ArrayList<MemberClass> theList;
    Button updateButton;
    ProgressBar updateProgressBar;
    String myTime;
    TextView myAlarmTimeTextView;
    Switch setAlarmSwitch;
    Boolean AlarmActive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inside_group_screen);
        updateButton = (Button) findViewById(R.id.updateButton);
        updateProgressBar = (ProgressBar)findViewById(R.id.updateProgressBar);
        myAlarmTimeTextView = (TextView)findViewById(R.id.myAlarmTimeTextView);
        setAlarmSwitch = (Switch)findViewById(R.id.InsideGroupSwitch);
        setAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = settings.edit();
                String AlarmActiveKey = "AlarmActive" + groupId;
                editor.putBoolean(AlarmActiveKey,isChecked);
                editor.commit();
                if(isChecked) {
                    setAlarm(myTime);
                }
                else{
                    cancelAlarm();
                }

            }
        });
        //Sets the ActionBarTitle to the groupName
        groupId = getIntent().getStringExtra("groupId");
        setTitle(groupId);
        settings = getSharedPreferences("settings",0);
        accountName = settings.getString("accountName",null);
        theList = new ArrayList<MemberClass>();

        membersListView = (ListView)findViewById(R.id.membersListView);
        memberListItemAdapter = new MemberListItemAdapter(this, R.layout.list_element_members, theList);
        membersListView.setAdapter(memberListItemAdapter);

        membersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MemberClass memberClass = (MemberClass) membersListView.getItemAtPosition(position);
                String memberName = memberClass.getAccount_name();
                Intent intent = new Intent(getApplicationContext(), sendSnoozeString.class);
                intent.putExtra("groupId", groupId);
                intent.putExtra("accountName", memberName);
                Log.d("DEBUG", groupId + " " + memberName);
                startActivity(intent);
            }


        });
    }





    private void cancelAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), groupId.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();

    }


    @Override
    protected void onResume() {
        super.onResume();
        updateMembers();
        String AlarmTimeKey = "myTime" + groupId;
        String AlarmActiveKey = "AlarmActive" + groupId;
        myTime = settings.getString(AlarmTimeKey,"0000");
        if(myTime!=null) {
            myAlarmTimeTextView.setText(myTime.substring(0,2)+ ":" + myTime.substring(2,4));
        }
        AlarmActive = settings.getBoolean(AlarmActiveKey,false);
        setAlarmSwitch.setChecked(AlarmActive);
    }




    // This method sets the alarm
    private void setAlarm(String myTime) {
        Calendar alarmTime = new GregorianCalendar();
        alarmTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(myTime.substring(0,2)));
        alarmTime.set(Calendar.MINUTE, Integer.parseInt(myTime.substring(2, 4)));
        alarmTime.set(Calendar.SECOND, 0);
        alarmTime.set(Calendar.MILLISECOND, 0);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("groupId", groupId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), groupId.hashCode(), intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmTime.getTimeInMillis() < System.currentTimeMillis()){
            alarmTime.add(Calendar.DAY_OF_YEAR, 1);
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
        SetAlarmTask setAlarmTask = new SetAlarmTask(accountName, groupId, myTime, 1);
        setAlarmTask.execute();
    }

    private void fillTheList(String membersString) {
        Scanner sc = new Scanner(membersString);
        String name;
        String alarmTime;
        int status;
        int image;
        while(sc.hasNext()){
            name = sc.next();
            alarmTime = sc.next();
            status = sc.nextInt();
            if(status==0)
                image = R.drawable.alarm_grey;
            else if(status==1)
                image = R.drawable.alarm_green;
            else
                image = R.drawable.alarm_black;

            theList.add(new MemberClass(image,name,alarmTime));
        }
    }

    public void setAlarmButtonPressed(View v){
        Intent theIntent = new Intent(this, SetAlarm.class);
        theIntent.putExtra("groupId", groupId);
        startActivity(theIntent);
    }

    public void updateButtonPressed(View v){
        updateMembers();

    }

    //Add stuff that updates the member list
    public void updateMembers(){
        theList.clear();
        memberListItemAdapter.notifyDataSetChanged();
        updateProgressBar.setVisibility(View.VISIBLE);
        DownloadMembersTask downloadMembersTask = new DownloadMembersTask();
        downloadMembersTask.execute();
        updateButton.setEnabled(false);
    }

    public void AlarmActiveButtonPressed(View v){
        //Add stuff to turn off/on the alarm

        //Test Stuff
        if(AlarmActiveButton.getText().equals("Alarm Active")){
            AlarmActiveButton.setText("Alarm Inactive");
        }
        else{
            AlarmActiveButton.setText("Alarm Active");
        }

        /*
            // This method cancels the alarm
        public void cancelAlarm(View view) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm canceled", Toast.LENGTH_LONG).show();

        status = INACTIVE_ALARM;
        //TODO database stuff

    }
         */


    }





    //Auto generated stuff for menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inside_group, menu);
        return true;
    }

    //Auto generated stuff for menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DownloadMembersTask extends AsyncTask<String, Void, Void> {

        String serverURL = "https://script.google.com/macros/s/AKfycbzq3AKQZ-GgQEzXdpIpNjG5nvzB89hDQ-wZAkrRudRbOkoQ5AiQ/exec";


        @Override
        protected Void doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            String serverURLandParams = serverURL +"?accountName="+ accountName + "&groupId=" + groupId; //creates a new String containing the scripts URL and the parameters
            HttpGet httpGet = new HttpGet(serverURLandParams);
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                membersString = EntityUtils.toString(httpResponse.getEntity()); // The returned String is saved
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        //This method is automatically when doInBackground is complete, in this case starting starting the new activity by calling startGroupActivity-method


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateProgressBar.setVisibility(View.GONE);
            fillTheList(membersString);
            memberListItemAdapter.notifyDataSetChanged();
            updateButton.setEnabled(true);
        }


    }
}

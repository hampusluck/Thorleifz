package thorleifz.wakeup;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * When you click on a group you get to this activity where you can see all members of it.
 * You should also be able to set your own alarm and see other peoples alarm statuses.
 */
public class InsideGroup extends ActionBarActivity {

    ListView membersListView;
<<<<<<< HEAD
    MemberListItemAdapter memberListItemAdapter;
    String groupName;
    String membersString;
=======
    ArrayAdapter arrayAdapter;
    String groupId;
>>>>>>> featureAlarmSync
    Button AlarmActiveButton;
    ArrayList<MemberClass> theList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inside_group_screen);

<<<<<<< HEAD
        //Sets the ActionBarTitle to the groupName
        groupName = getIntent().getStringExtra("groupName");
        setTitle(groupName);
        membersString = getIntent().getStringExtra("members");
        theList = new ArrayList<MemberClass>();
        FillArrayList(membersString);
=======
        //Sets the ActionBarTitle to the groupId
        groupId = getIntent().getStringExtra("groupId");
        setTitle(groupId);
>>>>>>> featureAlarmSync

        membersListView = (ListView)findViewById(R.id.membersListView);
        memberListItemAdapter = new MemberListItemAdapter(this, R.layout.list_element_members, theList);
        membersListView.setAdapter(memberListItemAdapter);
    }

    private void FillArrayList(String membersString) {
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
                image = R.drawable.alarm_black;
            else if(status==1)
                image = R.drawable.alarm_green;
            else
                image = R.drawable.alarm_grey;

            theList.add(new MemberClass(image,name,alarmTime));
        }

    }

    public void setAlarmButtonPressed(View v){
        Intent theIntent = new Intent(this, SetAlarm.class);
        theIntent.putExtra("groupId", groupId);
        startActivity(theIntent);
    }

    public void updateMembers(View v){
        //Add stuff that updates the member list
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
}

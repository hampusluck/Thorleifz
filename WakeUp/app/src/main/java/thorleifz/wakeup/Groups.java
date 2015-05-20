package thorleifz.wakeup;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * This is the activity where you can find all groups that you are a member of and you you reach
 * it when you have logged in.
 *
 * Created by rebeccaharkonen on 2015-04-24.
 */
public class Groups extends ActionBarActivity {

    private ListView groupList;

    private String groupString; // a single string that contains all group names
    SharedPreferences settings;
    String accountName;
    ArrayList<GroupClass> theList;

    private GroupListItemAdapter theAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_screen);
        settings = getSharedPreferences("settings",0);
        accountName = settings.getString("accountName", null);
        groupString = getIntent().getStringExtra("groups");
        theList = new ArrayList<GroupClass>();
        groupList = (ListView) findViewById(R.id.groupList);

        //Initierar adaptern och skickar med listan så att den vet vad den ska fylla raderna med
        theAdapter = new GroupListItemAdapter(getApplicationContext(), R.layout.list_element_groups, theList);
        groupList.setAdapter(theAdapter);

        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupClass groupClass = (GroupClass) groupList.getItemAtPosition(position);
                String groupId = groupClass.getGroup_id();
                    Intent intent = new Intent(getApplicationContext(), InsideGroup.class);
                    intent.putExtra("groupId", groupId);
                    startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(groupString!=null)
            fillArrayFromString(groupString);
            theAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_groups, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            clearAlarms();

            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearAlarms() {
        for(GroupClass g:theList){
            String groupId = g.getGroup_id();
            Intent intent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), groupId.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntent.cancel();

        }
        SetAlarmInactiveTask theTask = new SetAlarmInactiveTask();
        theTask.execute();
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }

    //Takes a string of several group names and stores them in the groupArray
    private void fillArrayFromString(String s){
        theList.clear();
        Scanner sc = new Scanner(s);
        while(sc.hasNext()){
            String groupId = sc.next();
            String AlarmTimeKey = "myTime" + groupId;
            String myTime = settings.getString(AlarmTimeKey,"0000");

            String AlarmActiveKey = "AlarmActive" + groupId;
            Boolean AlarmActive = settings.getBoolean(AlarmActiveKey,false);
            int status;
            if(AlarmActive)
                status = R.drawable.alarm_green;
            else
                status = R.drawable.alarm_grey;
            theList.add(new GroupClass(status, groupId, myTime.substring(0,2)+":"+myTime.substring(2,4)));
        }

    }

    public void newGroupButtonPressed(View v) {
        Intent theIntent = new Intent(this, AddGroup.class);
        startActivity(theIntent);
    }

    @Override
    public void onBackPressed(){} //Overriding this method makes it impossible to go back to mainActivity

    private class SetAlarmInactiveTask extends AsyncTask<String, Void, Void> {

        String serverURL = "https://script.google.com/macros/s/AKfycbyKYa8rYf382qq0dzabyEXb4JLeEIUoGr3D8sy1cnY5dlYIK_Te/exec";


        @Override
        protected Void doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            String serverURLandParams = serverURL + "?accountName=" + accountName; //creates a new String containing the scripts URL and the parameters
            HttpGet httpGet = new HttpGet(serverURLandParams);
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        //This method is automatically when doInBackground is complete, in this case starting starting the new activity by calling startGroupActivity-method
    }
}

package thorleifz.wakeup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
        if(groupString!=null) {
            fillArrayFromString(groupString);
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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
    //Takes a string of several group names and stores them in the groupArray
    private void fillArrayFromString(String s){
        Scanner sc = new Scanner(s);
        int i = 0;
        while(sc.hasNext()){
            if((i%2)==0)
                theList.add(new GroupClass(R.drawable.alarm_green, sc.next(), "11:00"));
            else
                theList.add(new GroupClass(R.drawable.alarm_grey, sc.next(), "11:00"));
            i++;
        }

    }

    public void newGroupButtonPressed(View v) {
        Intent theIntent = new Intent(this, AddGroup.class);
        startActivity(theIntent);
    }

    @Override
    public void onBackPressed(){} //Overriding this method makes it impossible to go back to mainActivity
}

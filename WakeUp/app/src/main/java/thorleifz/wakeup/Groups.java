package thorleifz.wakeup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * This is the activity where you can find all groups that you are a member of and you you reach
 * it when you have logged in.
 *
 * Created by rebeccaharkonen on 2015-04-24.
 */
public class Groups extends ActionBarActivity {

    private ListView groupList;

   // private ArrayAdapter arrayAdapter;
    private String groupString; // a single string that contains all group names
    private String[] groupArray; // a single array that contains all group names
    ArrayList<GroupClass> theList;

    private GroupListItemAdapter theAdapter;
    //private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_screen);
/*        groupArray = new String[]{"Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty"};*/
        groupString = getIntent().getStringExtra("groups");
        theList = new ArrayList<GroupClass>();
        if(groupString!=null) {
            fillArrayFromString(groupString);
        }
        groupList = (ListView) findViewById(R.id.groupList);

        //Skapar en lista med GroupClass objekt, varje GroupClass objekt innehåller information om en rad

/*        theList.add(new GroupClass(R.drawable.imup,"TestGroup1","21:00"));
        theList.add(new GroupClass(R.drawable.lozin,"TestGroup2","20:00"));
        theList.add(new GroupClass(R.drawable.imup,"TestGroup3","19:00"));*/

        //Initierar adaptern och skickar med listan så att den vet vad den ska fylla raderna med
        theAdapter = new GroupListItemAdapter(getApplicationContext(), R.layout.list_element, theList);
        groupList.setAdapter(theAdapter);


       // adapter = ... ;
       // groupList.setAdapter(adapter);

/*        int i = 0;
        for(String Name : groupArray ) // Create an object for each "grouprow"
        {
            GroupClass obj = new GroupClass(statusArray[i], Name, alarmArray[i]);
            theAdapter.add(obj);
            i++;
        }*/

        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupClass groupClass = (GroupClass) groupList.getItemAtPosition(position);
                String groupName = groupClass.getGroup_name();
                if(groupName!=null){
                Intent intent = new Intent(Groups.this, InsideGroup.class);
                intent.putExtra("groupName", groupName);
                startActivity(intent);}

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
        while(sc.hasNext()){
            theList.add(new GroupClass(R.drawable.lozin, sc.next(), "11:00"));
        }

    }

    public void newGroupButtonPressed(View v) {
        Intent theIntent = new Intent(this, AddGroup.class);
        startActivity(theIntent);
    }

    @Override
    public void onBackPressed(){} //Overriding this method makes it impossible to go back to mainActivity
}

package thorleifz.wakeup;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class InsideGroup extends ActionBarActivity {

    String [] members;
    ListView membersListView;
    ArrayAdapter arrayAdapter;
    String groupName;
    Button AlarmActiveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inside_group_screen);

        //Sets the ActionBarTitle to the groupName
        groupName = getIntent().getStringExtra("groupName");
        setTitle(groupName);

        //Gets the alarmbutton
        AlarmActiveButton = (Button)findViewById(R.id.alarmActiveButton);

        //Fills the list with test users
        members = new String[]{"TestUser1","TestUser2","TestUser3","TestUser4","TestUser5","TestUser6","TestUser7","TestUser8","TestUser9","TestUser10","TestUser11","TestUser12"};
        membersListView = (ListView)findViewById(R.id.membersListView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, members);
        membersListView.setAdapter(arrayAdapter);
        membersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = (String) membersListView.getItemAtPosition(position);
                Toast.makeText(InsideGroup.this, s + " selected", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setAlarmButtonPressed(View v){
        //Add stuff to set the alarm time
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

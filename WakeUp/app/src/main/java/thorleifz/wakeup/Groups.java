package thorleifz.wakeup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Scanner;

/**
 * Created by rebeccaharkonen on 2015-04-24.
 */
public class Groups extends Activity {

    private ListView groupList;
    private ArrayAdapter arrayAdapter;
    private String groupString; // a single string that contains all group names
    private String[] groupArray; // a single array that contains all group names

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_screen);
        groupString = getIntent().getStringExtra("groups");
        groupArray = new String[10];
        fillArrayFromString(groupString);
        groupList = (ListView) findViewById(R.id.groupList);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, groupArray);
        groupList.setAdapter(arrayAdapter);
        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = (String) groupList.getItemAtPosition(position);
                Toast.makeText(Groups.this, s + " selected", Toast.LENGTH_LONG).show();
            }
        });

    }
    //Takes a string of several group names and stores them in the groupArray
    private void fillArrayFromString(String s){
        Scanner sc = new Scanner(s);
        int i = 0;
        while(sc.hasNext()){
            groupArray[i++] = sc.next();
        }

    }

    public void newGroupButtonPressed(View v) {
        Intent theIntent = new Intent(this, NewGroup.class);
        startActivity(theIntent);
    }
}

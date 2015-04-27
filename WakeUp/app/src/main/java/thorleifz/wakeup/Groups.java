package thorleifz.wakeup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by rebeccaharkonen on 2015-04-24.
 */
public class Groups extends Activity {

    private ListView groupList;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_screen);

        groupList = (ListView) findViewById(R.id.groupList);

        // Get information from database and add into group-list
        String[] groups = new String[] { "Grupp 1", "Grupp 2", "Grupp 3",};

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, groups);
        groupList.setAdapter(arrayAdapter);
        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = (String) groupList.getItemAtPosition(position);
                Toast.makeText(Groups.this, s + " selected", Toast.LENGTH_LONG).show();
            }
        });

    }
/*    // TODO Lägga in metod onItemClick
    @Override
    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        String item = (String) groupList.getItemAtPosition(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
    }
*/
}

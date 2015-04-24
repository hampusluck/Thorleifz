package thorleifz.wakeup;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Created by rebeccaharkonen on 2015-04-24.
 */
public class Groups extends ListActivity {

    ListView groupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_screen);

        groupList = (ListView) findViewById(R.id.groupList);

        //Get information about groups from database
        String[] groups = new String[] { "Grupp 1",
                                         "Grupp 2",
                                         "Grupp 3"};


    }

}

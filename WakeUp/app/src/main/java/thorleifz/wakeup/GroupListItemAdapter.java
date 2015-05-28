package thorleifz.wakeup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Jacob on 2015-05-04.
 */
public class GroupListItemAdapter extends ArrayAdapter {

    private ImageView status;
    private TextView groupName;
    private TextView alarmTime;

    //When the adapter is created, the list of rows is saved
    public GroupListItemAdapter(Context context, int resource, ArrayList<GroupClass> theList) {
        super(context, resource, theList);
        this.theList = theList; //The list is saved
    }

    ArrayList<GroupClass> theList;

    //This method decides which information in the list goes where
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;

        //Deciding the layout of the row
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_element_groups, parent, false);

        }
        //Fetching everything from the layout
        status = (ImageView) row.findViewById(R.id.theStatusOnEachGroup);
        groupName = (TextView) row.findViewById(R.id.theGroupNameID);
        alarmTime = (TextView) row.findViewById(R.id.theAlarmID);

        //Filling them with information from the list
        status.setImageResource(theList.get(position).getStatus_resource());
        groupName.setText(theList.get(position).getGroup_id());
        alarmTime.setText(theList.get(position).getAlarm_time());

        return row;
    }
}



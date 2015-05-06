package thorleifz.wakeup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jacob on 2015-05-04.
 */
public class GroupListItemAdapter extends ArrayAdapter {

    ImageView status;
    TextView groupName;
    TextView alarmTime;

    ArrayList<GroupClass> theList;

    //När adaptern skapas tas listan med information om raderna och sparas
    public GroupListItemAdapter(Context context, int resource, ArrayList<GroupClass> theList) {
        super(context, resource, theList);
        this.theList = theList; //Listan sparas
    }

    //Den här metoden styr upp vilken information i listan som ska hamna var i raden
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;

        //Här är något autogenererat som bestämmer vilken layout raden ska ha
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_element, parent, false);

        }
        //Här hämtas alla saker från layouten
        status = (ImageView) row.findViewById(R.id.theStatusOnEachGroup);
        groupName = (TextView) row.findViewById(R.id.theGroupNameID);
        alarmTime = (TextView) row.findViewById(R.id.theAlarmID);

        //Här fylls de med information från listan
        status.setImageResource(theList.get(position).getStatus_resource());
        groupName.setText(theList.get(position).getGroup_name());
        alarmTime.setText(theList.get(position).getAlarm_time());

        return row;
    }
}



package thorleifz.wakeup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jacob on 2015-05-04.
 */
public class GroupListItemAdapter extends ArrayAdapter {


    private String[] alarmArray = {"a", "b", "c"}; // a single array that contains all alarms (testMode)
    private int[] statusArray = {1, 2, 3}; // a single array that contains all statuses (testMode)
    int[] statusPicturesArray = {R.drawable.imup, R.drawable.lozin, R.drawable.sleep}; // a single array that contains all the user-status images




    List theList = new ArrayList();

    public GroupListItemAdapter(Context context, int resource) {
        super(context, resource);
    }

    private static class DataHandler {  // Class to hold the data for each row (each group)
        ImageView status;               // How do we get the data from every group into this class? (we could use a "dataprovider"-class
        TextView groupName;
        TextView alarmTime;
    }

    @Override
    public void add(Object object) {
        super.add(object);
        theList.add(object);
    }

    @Override
    public int getCount() {
        return this.theList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.theList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        DataHandler handler;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_element, parent, false);
            handler = new DataHandler();
            handler.status = (ImageView) row.findViewById(R.id.theStatusOnEachGroup); //här behöver datan finnas tillgänglig. Kan göras med hjälp av en dataproviderklass.
            handler.groupName = (TextView) row.findViewById(R.id.theGroupNameID);
            handler.alarmTime = (TextView) row.findViewById(R.id.theAlarmID);
            row.setTag(handler);
        }
        else {
            handler = (DataHandler) row.getTag();
        }

        GroupClass eachGroup = (GroupClass) getItem(position);
        handler.groupName.setText(eachGroup.getGroup_name());
        handler.alarmTime.setText(eachGroup.getAlarm_time() );
        handler.status.setImageResource(eachGroup.getStatus_resource());

        return row; // super.getView(position, convertView, parent);
    }
}



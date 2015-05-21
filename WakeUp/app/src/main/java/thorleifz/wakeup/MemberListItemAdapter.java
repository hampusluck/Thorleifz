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
public class MemberListItemAdapter extends ArrayAdapter {

    ImageView status;
    TextView memberName;
    TextView alarmTime;

    ArrayList<MemberClass> theList;

    //When the adapter is created, the list of rows is saved
    public MemberListItemAdapter(Context context, int resource, ArrayList<MemberClass> theList) {
        super(context, resource, theList);
        this.theList = theList; //Listan sparas
    }

    //This method decides which information in the list goes where
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;

        //Deciding the layout of the row
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_element_members, parent, false);

        }
        //Fetching everything from the layout
        status = (ImageView) row.findViewById(R.id.memberStatusImageView);
        memberName = (TextView) row.findViewById(R.id.memberNameTextView);
        alarmTime = (TextView) row.findViewById(R.id.memberAlarmTextView);

        //Filling them with information from the list
        status.setImageResource(theList.get(position).getStatus_resource());
        memberName.setText(theList.get(position).getAccount_name());
        alarmTime.setText(theList.get(position).getAlarm_time().substring(0,2)+":"+theList.get(position).getAlarm_time().substring(2,4));

        return row;
    }
}



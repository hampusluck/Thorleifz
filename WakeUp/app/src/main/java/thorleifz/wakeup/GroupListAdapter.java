package thorleifz.wakeup;

import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.app.Activity;
import java.util.List;

/**
 * Created by rebeccaharkonen on 2015-04-27.
 */
public class GroupListAdapter extends ArrayAdapter{

    private Context context;
    private boolean useList = true;

    public GroupListAdapter(Context context, List items) {
        super(context, android.R.layout.simple_list_item_2, items);
        this.context = context;
    }

    /**
     * Holder for the list items.
     * This private class is used to enhance the list loading speed.
     */
    private class ViewHolder{
        TextView titleText;
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        GroupListItem item = (GroupListItem)getItem(position);
        View viewToUse = null;

        // This block exists to inflate the settings list item conditionally based on whether
        // we want to support a grid or list view.
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            if(useList){
                viewToUse = mInflater.inflate(R.layout.group_list_item, null);
            } else {
                viewToUse = mInflater.inflate(R.layout.group_grid_item, null);
            }

            holder = new ViewHolder();
            holder.titleText = (TextView)viewToUse.findViewById(R.id.groupListItemText);
            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }

        holder.titleText.setText(item.getGroupTitle());
        return viewToUse;
    }
}

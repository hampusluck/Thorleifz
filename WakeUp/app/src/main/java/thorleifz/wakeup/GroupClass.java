package thorleifz.wakeup;

/**
 * a GroupClass is a class representing a group in the Groups activity.
 * Each entry in the list seen in the Groups activity corresponds to a GroupClass object.
 * Created by Jacob on 2015-05-05.
 */
public class GroupClass {

    private int status_resource;
    private String group_id;
    private String alarm_time;

    public GroupClass(int status_resource, String group_id, String alarm_time) {
        this.status_resource = status_resource;
        this.group_id = group_id;
        this.alarm_time = alarm_time;

    }
    public int getStatus_resource() {
        return status_resource;
    }
    public void setStaus_resource(int status_resource) {
        this.status_resource = status_resource;
    }
    public String getGroup_id() {
        return group_id;
    }
    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }
    public String getAlarm_time() {
        return alarm_time;
    }
    public void setAlarm_time(String alarm_time) {
        this.alarm_time = alarm_time;
    }
}

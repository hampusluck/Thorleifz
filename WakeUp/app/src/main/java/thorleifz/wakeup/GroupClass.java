package thorleifz.wakeup;

/**
 * Created by Jacob on 2015-05-05.
 */
public class GroupClass {

    private int status_resource;
    private String group_name;
    private String alarm_time;

    public GroupClass(int status_resource, String group_name, String alarm_time) {
        this.status_resource = status_resource;
        this.group_name = group_name;
        this.alarm_time = alarm_time;
    }
    public int getStatus_resource() {
        return status_resource;
    }
    public void setStaus_resource(int status_resource) {
        this.status_resource = status_resource;
    }
    public String getGroup_name() {
        return group_name;
    }
    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }
    public String getAlarm_time() {
        return alarm_time;
    }
    public void setAlarm_time(String alarm_time) {
        this.alarm_time = alarm_time;
    }

}

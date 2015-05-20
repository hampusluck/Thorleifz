package thorleifz.wakeup;

/**
 * Created by Jacob on 2015-05-05.
 */
public class MemberClass {

    private int status_resource;
    private String account_name;
    private String alarm_time;

    public MemberClass(int status_resource, String account_name, String alarm_time) {
        this.status_resource = status_resource;
        this.account_name = account_name;
        this.alarm_time = alarm_time;
    }
    public int getStatus_resource() {
        return status_resource;
    }
    public void setStaus_resource(int status_resource) {
        this.status_resource = status_resource;
    }
    public String getAccount_name() {
        return account_name;
    }
    public void setAccount_name(String group_name) {
        this.account_name = group_name;
    }
    public String getAlarm_time() {
        return alarm_time;
    }
    public void setAlarm_time(String alarm_time) {
        this.alarm_time = alarm_time;
    }

}

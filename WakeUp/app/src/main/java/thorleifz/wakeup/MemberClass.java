package thorleifz.wakeup;

/**
 * Created by Jacob on 2015-05-05.
 */
public class MemberClass {

    private int status_resource;
    private String member_name;
    private String alarm_time;

    public MemberClass(int status_resource, String member_name, String alarm_time) {
        this.status_resource = status_resource;
        this.member_name = member_name;
        this.alarm_time = alarm_time;
    }
    public int getStatus_resource() {
        return status_resource;
    }
    public void setStaus_resource(int status_resource) {
        this.status_resource = status_resource;
    }
    public String getMember_name() {
        return member_name;
    }
    public void setMember_name(String group_name) {
        this.member_name = group_name;
    }
    public String getAlarm_time() {
        return alarm_time;
    }
    public void setAlarm_time(String alarm_time) {
        this.alarm_time = alarm_time;
    }

}

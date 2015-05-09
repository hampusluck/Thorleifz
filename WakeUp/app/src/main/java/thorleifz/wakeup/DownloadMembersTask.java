package thorleifz.wakeup;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by adammyren on 15-04-29.
 */
//private class that runs the back-end script in a seperate thread
public class DownloadMembersTask extends AsyncTask<String, Void, String> {

    String serverURL = "https://script.google.com/macros/s/AKfycbzq3AKQZ-GgQEzXdpIpNjG5nvzB89hDQ-wZAkrRudRbOkoQ5AiQ/exec";
    String accountName;
    String theMembers;
    String groupId;
    Context context;
    //The doInBackground-method is called when the object is executed
    public DownloadMembersTask(String accountName, String groupId, Context context){
        this.accountName = accountName;
        this.groupId = groupId;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpClient httpClient = new DefaultHttpClient();
        String serverURLandParams = serverURL +"?accountName="+ accountName + "&groupId=" + groupId; //creates a new String containing the scripts URL and the parameters
        HttpGet httpGet = new HttpGet(serverURLandParams);
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            theMembers = EntityUtils.toString(httpResponse.getEntity()); // The returned String is saved
        } catch (IOException e) {
            e.printStackTrace();
        }
        return theMembers;
    }
    //This method is automatically when doInBackground is complete, in this case starting starting the new activity by calling startGroupActivity-method
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        startInsideGroupActivity();
    }

    //Starts the group activity and passes a single String containing all the group names
    public void startInsideGroupActivity(){
        Intent intent = new Intent(context, InsideGroup.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("groupName", groupId);
        intent.putExtra("members",theMembers);
        context.startActivity(intent);
    }
}

package thorleifz.wakeup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
public class DownloadGroupsTask extends AsyncTask<String, Void, String> {

    String serverURL = "https://script.google.com/macros/s/AKfycbxu0F2ua8I8iu5NheQ8F6uzTiju1MLjLk_29-HU3jfoSfrhkeT0/exec";
    String accountName;
    String theGroups;
    Context context;
    //The doInBackground-method is called when the object is executed
    public DownloadGroupsTask(String accountName, Context context){
        this.accountName = accountName;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpClient httpClient = new DefaultHttpClient();
        String serverURLandParams = serverURL +"?accountName="+ accountName; //creates a new String containing the scripts URL and the parameters
        HttpGet httpGet = new HttpGet(serverURLandParams);
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            theGroups = EntityUtils.toString(httpResponse.getEntity()); // The returned String is saved
        } catch (IOException e) {
            e.printStackTrace();
        }
        return theGroups;
    }
    //This method is automatically when doInBackground is complete, in this case starting starting the new activity by calling startGroupActivity-method
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        startGroupActivity();
    }

    //Starts the group activity and passes a single String containing all the group names
    public void startGroupActivity(){
        Intent intent = new Intent(context, Groups.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("groups",theGroups);
        context.startActivity(intent);
    }
}

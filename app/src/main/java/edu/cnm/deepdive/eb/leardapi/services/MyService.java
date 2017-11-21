package edu.cnm.deepdive.eb.leardapi.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.gson.Gson;
import edu.cnm.deepdive.eb.leardapi.model.GoogleItem;
import edu.cnm.deepdive.eb.leardapi.utils.HttpHelper;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MyService extends IntentService {

    public static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Uri uri = intent.getData();
        Log.i(TAG, "onHandleIntent: " + uri.toString());


        String response;
        try {
            response = HttpHelper.downloadUrl(uri.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        JSONArray firstArray = new JSONArray();
        // Get individual image link
        JSONArray imageLinks = new JSONArray();

        try {
            JSONObject jsonObj = new JSONObject(response);
            firstArray = jsonObj.getJSONArray("items");

            for (int i = 0; i < firstArray.length(); i++) {
                imageLinks.put(firstArray.getJSONObject(i).getString("link"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        // change this to items from my link

        GoogleItem[] dataItems = gson.fromJson(firstArray.toString(), GoogleItem[].class);

        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        messageIntent.putExtra(MY_SERVICE_PAYLOAD, dataItems);
        LocalBroadcastManager manager =
                LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

}

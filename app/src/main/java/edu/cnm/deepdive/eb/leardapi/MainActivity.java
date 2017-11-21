package edu.cnm.deepdive.eb.leardapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.eb.leardapi.model.GoogleItem;
import edu.cnm.deepdive.eb.leardapi.services.MyService;
import edu.cnm.deepdive.eb.leardapi.utils.NetworkHelper;

public class MainActivity extends AppCompatActivity {

  private static final String JSON_URL;

  private boolean networkOk;
  TextView output;



  private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
//      String message =
//          intent.getStringExtra(MyService.MY_SERVICE_PAYLOAD);

      GoogleItem[] dataItems = (GoogleItem[]) intent
          .getParcelableArrayExtra(MyService.MY_SERVICE_PAYLOAD);

      GridLayout imageLayout = (GridLayout) findViewById(R.id.image_layout);

      ImageView[] imageView = new ImageView[dataItems.length];


        for (int i = 0; i < dataItems.length; i++) {
          imageView[i] = new ImageView(context);
          imageView[i].setPadding(5, 5, 5, 5);

          Picasso.with(context)
              .load(dataItems[i].getLink()).resize(400, 350).into(imageView[i]);
          imageLayout.addView(imageView[i]);
          }
//          imageLayout.removeAllViews();

      }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

//    output = (TextView) findViewById(R.id.output);






    LocalBroadcastManager.getInstance(getApplicationContext())
        .registerReceiver(mBroadcastReceiver,
            new IntentFilter(MyService.MY_SERVICE_MESSAGE));

    networkOk = NetworkHelper.hasNetworkAccess(this);
//    output.append("Network ok: " + networkOk);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    LocalBroadcastManager.getInstance(getApplicationContext())
        .unregisterReceiver(mBroadcastReceiver);
  }

  public void runClickHandler(View view) {

    if (networkOk) {
      Intent intent = new Intent(this, MyService.class);
      intent.setData(Uri.parse(JSON_URL));
      startService(intent);
    } else {
      Toast.makeText(this, "Network not available!", Toast.LENGTH_SHORT).show();
    }
  }

  public void clearClickHandler(View view) {
    output.setText("");
  }

}

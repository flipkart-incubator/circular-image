package com.flipkart.circularImageView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int widthPixels = getResources().getDisplayMetrics().widthPixels;

        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.katrina_kaif);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.jennifer_aniston);
        Bitmap badgeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.flipkart_round);
        Log.v(MainActivity.class.getName(), "Height:" + bitmap1.getHeight() + " Width:" + bitmap1.getWidth());

        //Test Image 1
        ImageView testIcon = (ImageView) findViewById(R.id.iv_test_icon_1);
        CircularDrawable circularDrawable = new CircularDrawable();
        circularDrawable.setBitmapOrText(bitmap1);
        circularDrawable.setNotificationText("5");
        circularDrawable.setNotificationAngle(45);
        circularDrawable.setNotificationStyle(CircularDrawable.NotificationStyle.Circle);
        circularDrawable.setNotificationColor(Color.BLACK, Color.parseColor("#FDC301"));
        circularDrawable.setBadge(badgeIcon);
//        circularDrawable.setBorder(Color.BLACK, 8);
        testIcon.setImageDrawable(circularDrawable);


        //Test Image 2
        testIcon = (ImageView) findViewById(R.id.iv_test_icon_2);
        circularDrawable = new CircularDrawable();
        circularDrawable.setBitmapOrText(bitmap2);
        circularDrawable.setNotificationText("51");
        circularDrawable.setNotificationAngle(45);
//        circularDrawable.setNotificationStyle(CircularDrawable.NotificationStyle.Circle);
//        circularDrawable.setNotificationColor(Color.BLACK, Color.parseColor("#FDC301"));
        circularDrawable.setBadge(badgeIcon);
        circularDrawable.setBorder(Color.BLACK, 4);
        testIcon.setImageDrawable(circularDrawable);

        //Test Image 3
        testIcon = (ImageView) findViewById(R.id.iv_test_icon_3);
        circularDrawable = new CircularDrawable();
        circularDrawable.setBitmapOrText("VS");
//        circularDrawable.setNotificationText("5");
//        circularDrawable.setNotificationAngle(45);
//        circularDrawable.setNotificationStyle(CircularDrawable.NotificationStyle.Circle);
//        circularDrawable.setNotificationColor(Color.BLACK, Color.parseColor("#FDC301"));
        circularDrawable.setBadge(badgeIcon);
//        circularDrawable.setBorder(Color.BLACK, 4);
        testIcon.setImageDrawable(circularDrawable);

        //Test Image 4
        testIcon = (ImageView) findViewById(R.id.iv_test_icon_4);
        circularDrawable = new CircularDrawable();
        circularDrawable.setBitmapOrText("VS", bitmap2);
        circularDrawable.setNotificationText("5");
        circularDrawable.setNotificationAngle(135);
        circularDrawable.setNotificationStyle(CircularDrawable.NotificationStyle.Circle);
        circularDrawable.setNotificationColor(Color.WHITE, Color.RED);
//        circularDrawable.setBadge(badgeIcon);
//        circularDrawable.setBorder(Color.BLACK, 4);
        testIcon.setImageDrawable(circularDrawable);

        //Test Image 5
        testIcon = (ImageView) findViewById(R.id.iv_test_icon_5);
        circularDrawable = new CircularDrawable();
        circularDrawable.setBitmapOrText("VS", bitmap2, bitmap1);
//        circularDrawable.setNotificationText("5");
//        circularDrawable.setNotificationAngle(135);
//        circularDrawable.setNotificationStyle(CircularDrawable.NotificationStyle.Circle);
//        circularDrawable.setNotificationColor(Color.WHITE, Color.RED);
//        circularDrawable.setBadge(badgeIcon);
        circularDrawable.setBorder(Color.MAGENTA, 8);
        testIcon.setImageDrawable(circularDrawable);

        //Test Image 6
        testIcon = (ImageView) findViewById(R.id.iv_test_icon_6);
        circularDrawable = new CircularDrawable();
        circularDrawable.setBitmapOrText("VS", bitmap2, bitmap1, "AB");
//        circularDrawable.setNotificationText("5");
//        circularDrawable.setNotificationAngle(135);
//        circularDrawable.setNotificationStyle(CircularDrawable.NotificationStyle.Circle);
//        circularDrawable.setNotificationColor(Color.WHITE, Color.RED);
//        circularDrawable.setBadge(badgeIcon);
//        circularDrawable.setBorder(Color.BLUE, 8);
        testIcon.setImageDrawable(circularDrawable);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

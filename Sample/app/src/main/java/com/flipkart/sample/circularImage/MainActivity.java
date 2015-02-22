package com.flipkart.sample.circularImage;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;

import com.flipkart.sample.circularImage.R;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ImageLoader().execute();
    }

    private class ImageLoader extends AsyncTask<Void, Void, Void> {
        private CircularDrawable circularDrawable1;
        private CircularDrawable circularDrawable2;
        private CircularDrawable circularDrawable3;
        private CircularDrawable circularDrawable4;
        private CircularDrawable circularDrawable5;
        private CircularDrawable circularDrawable6;
        private ProgressDialog loadingDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = ProgressDialog.show(MainActivity.this, null, "Loading");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.katrina_kaif);
            Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.jennifer_aniston);
            Bitmap badgeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.flipkart_round);
            Log.v(MainActivity.class.getName(), "Height:" + bitmap1.getHeight() + " Width:" + bitmap1.getWidth());

            //Test Image 1
            circularDrawable1 = new CircularDrawable();
            circularDrawable1.setBitmapOrText(bitmap1);
            circularDrawable1.setNotificationText("5");
            circularDrawable1.setNotificationAngle(45);
            circularDrawable1.setNotificationStyle(CircularDrawable.NotificationStyle.Circle);
            circularDrawable1.setNotificationColor(Color.BLACK, Color.parseColor("#FDC301"));
            circularDrawable1.setBadge(badgeIcon);
//          circularDrawable1.setBorder(Color.BLACK, 8);

            //Test Image 2
            circularDrawable2 = new CircularDrawable();
            circularDrawable2.setBitmapOrText(bitmap2);
            circularDrawable2.setNotificationText("5");
            circularDrawable2.setNotificationAngle(45);
//          circularDrawable2.setNotificationStyle(CircularDrawable.NotificationStyle.Circle);
//          circularDrawable2.setNotificationColor(Color.BLACK, Color.parseColor("#FDC301"));
            circularDrawable2.setBadge(badgeIcon);
            circularDrawable2.setBorder(Color.BLACK, 4);

            //Test Image 3
            circularDrawable3 = new CircularDrawable();
            circularDrawable3.setBitmapOrText("VS");
//          circularDrawable3.setNotificationText("5");
//          circularDrawable3.setNotificationAngle(45);
//          circularDrawable3.setNotificationStyle(CircularDrawable.NotificationStyle.Circle);
//          circularDrawable3.setNotificationColor(Color.BLACK, Color.parseColor("#FDC301"));
            circularDrawable3.setBadge(badgeIcon);
//          circularDrawable3.setBorder(Color.BLACK, 4);

            //Test Image 4
            circularDrawable4 = new CircularDrawable();
            circularDrawable4.setBitmapOrText("VS", bitmap2);
            circularDrawable4.setNotificationText("51");
            circularDrawable4.setNotificationAngle(135);
            circularDrawable4.setNotificationStyle(CircularDrawable.NotificationStyle.Circle);
            circularDrawable4.setNotificationColor(Color.WHITE, Color.RED);
//          circularDrawable4.setBadge(badgeIcon);
//          circularDrawable4.setBorder(Color.BLACK, 4);

            //Test Image 5
            circularDrawable5 = new CircularDrawable();
            circularDrawable5.setBitmapOrText("VS", bitmap2, bitmap1);
//          circularDrawable5.setNotificationText("5");
//          circularDrawable5.setNotificationAngle(135);
//          circularDrawable5.setNotificationStyle(CircularDrawable.NotificationStyle.Circle);
//          circularDrawable5.setNotificationColor(Color.WHITE, Color.RED);
//          circularDrawable5.setBadge(badgeIcon);
            circularDrawable5.setBorder(Color.MAGENTA, 8);

            //Test Image 6
            circularDrawable6 = new CircularDrawable();
            circularDrawable6.setBitmapOrText("VS", bitmap2, bitmap1, "AB");
//          circularDrawable6.setNotificationText("5");
//          circularDrawable6.setNotificationAngle(135);
//          circularDrawable6.setNotificationStyle(CircularDrawable.NotificationStyle.Circle);
//          circularDrawable6.setNotificationColor(Color.WHITE, Color.RED);
//          circularDrawable6.setBadge(badgeIcon);
//          circularDrawable6.setBorder(Color.BLUE, 8);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            loadingDialog.dismiss();

            ImageView testIcon = (ImageView) findViewById(R.id.iv_test_icon_1);
            testIcon.setImageDrawable(circularDrawable1);

            testIcon = (ImageView) findViewById(R.id.iv_test_icon_2);
            testIcon.setImageDrawable(circularDrawable2);

            testIcon = (ImageView) findViewById(R.id.iv_test_icon_3);
            testIcon.setImageDrawable(circularDrawable3);

            testIcon = (ImageView) findViewById(R.id.iv_test_icon_4);
            testIcon.setImageDrawable(circularDrawable4);

            testIcon = (ImageView) findViewById(R.id.iv_test_icon_5);
            testIcon.setImageDrawable(circularDrawable5);

            testIcon = (ImageView) findViewById(R.id.iv_test_icon_6);
            testIcon.setImageDrawable(circularDrawable6);
        }
    }
}

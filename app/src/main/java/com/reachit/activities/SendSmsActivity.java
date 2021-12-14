package com.reachit.activities;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.reachit.R;
import com.reachit.recievers.MessageSentListener;
import com.reachit.services.GPSTracker;

public class SendSmsActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private static final String SENT_SMS_FLAG = "SMS_SENT";

    double latitude, longitude;
    String phoneNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        final Window win = getWindow();
        win.addFlags( WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON );
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                phoneNo = null;
            } else {
                phoneNo = extras.getString("phn");
            }
        } else {
            phoneNo = (String) savedInstanceState.getSerializable("phn");
        }

        getLocation();

    }
    private void getLocation() {
        Log.i("long", "getting location");
        final GPSTracker gps = new GPSTracker(this);
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.i("lat", "" + latitude);
            Log.i("long", "" + longitude);
            sendSMSMessage();

        } else {
            // can't get location
            Log.i("long", "Cant get location");
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    protected void sendSMSMessage() {
        Log.i("long", "///////Sending message");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i("asd", "Check self permission");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
                Log.i("asd", "should show permission dialog");
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        } else {
            Log.i("long", "///////Permission granted");
            sendMessage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendMessage();
            } else {
                Toast.makeText(getApplicationContext(),
                        "SMS faild, please try again.", Toast.LENGTH_LONG).show();
            }
        }

    }

    BroadcastReceiver broadcastReceiver = new MessageSentListener();
    private void sendMessage() {
        SmsManager smsManager = SmsManager.getDefault();
        Intent intent = new Intent(SENT_SMS_FLAG);
        PendingIntent sentIntent = PendingIntent.getBroadcast(SendSmsActivity.this, 0,
                intent, 0);
        SendSmsActivity.this.registerReceiver(
                broadcastReceiver,
                new IntentFilter(SENT_SMS_FLAG));
        smsManager.sendTextMessage(phoneNo, null, "https://www.google.com/maps/@" + latitude + "," + longitude + ",21z" +
                "\nClick the Link above Or search Coordinates on google maps\n " +
                "Coordinates: " + latitude + ", " + longitude, null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.",
                Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        SendSmsActivity.this.unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}

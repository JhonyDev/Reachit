package com.lma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.lma.R;
import com.lma.services.GPSTracker;

public class SendSmsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        Intent i = getPackageManager().getLaunchIntentForPackage("com.lma");
        startActivity(i);
        String phoneNo = getIntent().getStringExtra("phoneNo");
        final GPSTracker gps = new GPSTracker(this);
        SmsManager smsManager = SmsManager.getDefault();
        double lat = gps.getLatitude();
        double lng = gps.getLongitude();
        smsManager.sendTextMessage(phoneNo, null, "device_location," + lat + "," + lng, null, null);

    }

}

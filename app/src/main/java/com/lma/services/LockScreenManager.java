package com.lma.services;

import static com.lma.application.App.CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.lma.MainActivity;
import com.lma.R;
import com.lma.activities.LockSettingsActivity;
import com.lma.recievers.ScreenStateReceiver;

import java.util.Timer;

public class LockScreenManager extends Service {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT2 = "text2";
    public static final String IMAGE_URI = "imagePath";

    public static String pattern;
    String previousKey;
    String prevImageURI;

    public static Timer timer;
    public static boolean timerRunning = false;
    public static boolean screenOn = true;
    public static String activationKey;
    public static boolean screamOn = false;
    public static boolean flashOn = false;

    public static boolean locationOn = false;
    public static Drawable imagBack;

    public static String imageURI;

    BroadcastReceiver mScreenStateReceiver;

    public LockScreenManager() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        loadData();

        if (LockSettingsActivity.imageURI != null){
            LockScreenManager.imageURI = LockSettingsActivity.imageURI;
            LockScreenManager.imagBack = LockSettingsActivity.drawable;
        }

        if (previousKey != null){
            LockScreenManager.activationKey = previousKey;
        }

        Log.i("Asd", "PASSWORD = " + activationKey);
        Log.i("Asd", "PATTERN = " + pattern);

        registerReceivers();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }


        LockScreenManager.screenOn = true;
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("ReachIt Security")
                .setContentText("ReachIt Services Running")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
        return START_NOT_STICKY;

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void registerReceivers() {
        IntentFilter screenStateFilter = new IntentFilter();
        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mScreenStateReceiver = new ScreenStateReceiver();
        registerReceiver(mScreenStateReceiver, screenStateFilter);

    }

    @Override
    public void onDestroy() {
        Log.i("laksjdl", "disabled On Destroy Called");
        unregisterReceiver(mScreenStateReceiver);
        saveData();
        stopSelf();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT2, LockScreenManager.activationKey);
        editor.putString(IMAGE_URI, LockScreenManager.imageURI);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        previousKey = sharedPreferences.getString(TEXT2, "reachit");
        prevImageURI = sharedPreferences.getString(IMAGE_URI, null);
    }
}

package com.reachit.recievers;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.reachit.R;
import com.reachit.services.GPSTracker;
import com.reachit.services.LockScreenManager;

public class SmsReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    String msg, phoneNo = "";
    String start = LockScreenManager.activationKey + " scream";
    String stop = LockScreenManager.activationKey + " stop";
    String location = LockScreenManager.activationKey + " send location";
    String blinkFlash = LockScreenManager.activationKey + " blink flash";

    MediaPlayer mediaPlayer;


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        Log.i("TAG", "msg Received");
        mediaPlayer = MediaPlayer.create(context, R.raw.all_might_i_am_here);
        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle dataBundle = intent.getExtras();
            if (dataBundle != null) {
                Object[] mypdu = (Object[]) dataBundle.get("pdus");
                final SmsMessage[] message = new SmsMessage[mypdu.length];
                for (int i = 0; i < mypdu.length; i++) {
                    String format = dataBundle.getString("format");
                    message[i] = SmsMessage.createFromPdu((byte[]) mypdu[i], format);
                    msg = message[i].getMessageBody();
                    phoneNo = message[i].getOriginatingAddress();
                }

                String command = msg.toLowerCase();
                Toast.makeText(context, command, Toast.LENGTH_SHORT).show();
                if (command.equals(start) && LockScreenManager.screamOn) {
                    Toast.makeText(context, "Starting", Toast.LENGTH_SHORT).show();
                    mediaPlayer.setLooping(true);
                    AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    int values = 15;
                    if (mgr != null) {
                        mgr.setStreamVolume(AudioManager.STREAM_MUSIC, values, 0);
                    }
                    mediaPlayer.start();
                }
                if (command.equals(stop) && LockScreenManager.screamOn) {
                    Toast.makeText(context, "Stopping", Toast.LENGTH_SHORT).show();
                    AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    int values = 0;
                    if (mgr != null) {
                        mgr.setStreamVolume(AudioManager.STREAM_MUSIC, values, 0);
                    }
                    mediaPlayer.setLooping(false);
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }

                if (command.equals(location) && LockScreenManager.locationOn) {
                    GPSTracker gps = new GPSTracker(context);
                    if (gps.canGetLocation()) {
                        Intent intent1 = new Intent();
                        intent1.setComponent(new ComponentName("com.reachit", "com.reachit.activities.SendSmsActivity"));
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.putExtra("phn", phoneNo);
                        context.startActivity(intent1);
                    } else {
                        // can't get location
                        Log.i("long", "///////Cant get location");
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();
                    }
                }
                if (command.equals(blinkFlash) && LockScreenManager.flashOn){
                    Intent intent1 = new Intent();
                    intent1.setComponent(new ComponentName("com.reachit", "com.reachit.activities.FlashBlinkActivity"));
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                }
            }
        }
    }

}

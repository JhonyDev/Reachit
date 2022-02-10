package com.lma.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.lma.info.Info;
import com.lma.utils.SharedPrefUtils;

public class SimStateReceiver extends BroadcastReceiver implements Info {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SharedPrefUtils.getBooleanSharedPrefs(context, KEY_SEND_SMS))
            sendMessage(context);
    }

    private void sendMessage(Context context) {
        String phoneNo = SharedPrefUtils.getStringSharedPrefs(context, KEY_EMERGENCY_CONTACT);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, "ALERT : SIM STATE CHANGED", null, null);
    }
}

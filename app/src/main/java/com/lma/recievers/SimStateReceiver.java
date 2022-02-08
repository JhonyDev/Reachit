package com.lma.recievers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.lma.info.Info;
import com.lma.utils.SharedPrefUtils;

public class SimStateReceiver extends BroadcastReceiver implements Info {

    private static final String SIM_STATE_RECEIVED = "android.provider.Telephony.SIM_STATE_CHANGED";

    @Override
    public void onReceive(Context context, Intent intent) {
        sendMessage(context);
    }

    private void sendMessage(Context context) {
        String phoneNo = SharedPrefUtils.getStringSharedPrefs((Activity) context, KEY_EMERGENCY_CONTACT);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, "ALERT : SIM STATE CHANGED", null, null);
    }
}

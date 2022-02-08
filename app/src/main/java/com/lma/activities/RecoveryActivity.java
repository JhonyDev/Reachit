package com.lma.activities;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lma.R;
import com.lma.info.Info;
import com.lma.recievers.SmsReceiver;
import com.lma.services.LockScreenManager;
import com.lma.utils.SharedPrefUtils;
import com.lma.utils.Utils;

public class RecoveryActivity extends AppCompatActivity implements Info, TextWatcher {

    Button btnSave;
    EditText etDevicePassword;
    EditText etEmergencyContact;
    String strEtDevicePassword;
    String strEtEmergencyContact;
    Switch switchTracking;
    Switch switchSendSMS;

    String currentEmergency;
    String currentPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);

        initViews();
        initUserData();
        initSwitches();
        initTextWatchers();

    }

    private void initTextWatchers() {
        etDevicePassword.addTextChangedListener(this);
        etEmergencyContact.addTextChangedListener(this);
    }

    private void initSwitches() {
        switchSendSMS.setOnCheckedChangeListener((compoundButton, b) -> {
            SharedPrefUtils.putBooleanSharedPrefs(RecoveryActivity.this, b, KEY_SEND_SMS);
            // TODO: SEND SMS ON SIM REMOVAL
        });
        switchTracking.setOnCheckedChangeListener((compoundButton, b) -> {
            SharedPrefUtils.putBooleanSharedPrefs(RecoveryActivity.this, b, KEY_TRACKING);
            if (b)
                enableBroadcastReceiver();
            else
                disableBroadcastReceiver();

        });
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (LockScreenManager.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void disableBroadcastReceiver() {
        ComponentName receiver = new ComponentName(this, SmsReceiver.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        Toast.makeText(this, "Broadcast receiver Disabled", Toast.LENGTH_SHORT).show();
    }

    private void enableBroadcastReceiver() {
        if (isMyServiceRunning()) {
            ComponentName receiver = new ComponentName(this, SmsReceiver.class);
            PackageManager pm = this.getPackageManager();
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
            Toast.makeText(this, "Broadcast receiver Enabled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Enable Lock Screen First", Toast.LENGTH_SHORT).show();
        }

    }

    private void initUserData() {
        currentEmergency = SharedPrefUtils.getStringSharedPrefs(this, KEY_EMERGENCY_CONTACT);
        currentPassword = SharedPrefUtils.getStringSharedPrefs(this, KEY_DEVICE_PASSWORD);

        switchTracking.setChecked(SharedPrefUtils.getBooleanSharedPrefs(this, KEY_TRACKING));
        switchSendSMS.setChecked(SharedPrefUtils.getBooleanSharedPrefs(this, KEY_SEND_SMS));

        etDevicePassword.setText(currentPassword);
        etEmergencyContact.setText(currentEmergency);


    }

    private void initViews() {
        btnSave = findViewById(R.id.btn_save);
        etDevicePassword = findViewById(R.id.et_user_name);
        etEmergencyContact = findViewById(R.id.et_email);
        switchTracking = findViewById(R.id.switch_tracking);
        switchSendSMS = findViewById(R.id.switch_send_sms);
    }

    public void back(View view) {
        finish();
    }

    public void save(View view) {
        Log.i(TAG, "save: ");
        castStrings();
        if (!Utils.validEt(etDevicePassword, strEtDevicePassword))
            return;
        if (!Utils.validEt(etEmergencyContact, strEtEmergencyContact))
            return;

        try {
            Utils.currentDevice.setPassword(strEtDevicePassword);
            Utils.getReference().child(NODE_DEVICES).child(Utils.getCurrentUserId())
                    .child(Utils.currentDevice.getIMEI())
                    .setValue(Utils.currentDevice).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    btnSave.setVisibility(View.GONE);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPrefUtils.putStringSharedPrefs(this, strEtEmergencyContact, KEY_EMERGENCY_CONTACT);
        SharedPrefUtils.putStringSharedPrefs(this, strEtDevicePassword, KEY_DEVICE_PASSWORD);
    }

    private void castStrings() {
        strEtDevicePassword = etDevicePassword.getText().toString();
        strEtEmergencyContact = etEmergencyContact.getText().toString();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        castStrings();
        if (!strEtEmergencyContact.equals(currentEmergency)
                | !strEtDevicePassword.equals(currentPassword))
            btnSave.setVisibility(View.VISIBLE);
        else
            btnSave.setVisibility(View.GONE);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
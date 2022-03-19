package com.lma.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lma.R;
import com.lma.info.Info;
import com.lma.model.Device;
import com.lma.model.UserPojo;
import com.lma.utils.DialogUtils;
import com.lma.utils.SharedPrefUtils;
import com.lma.utils.Utils;

import java.util.Objects;

public class DashboardActivity extends AppCompatActivity implements Info {

    Dialog loadingDialog;
    EditText etIMEI;
    String strEtIMEI;
    String currentIMEI;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initPermissions();

        initViews();

        loadingDialog = new Dialog(this);
        DialogUtils.initLoadingDialog(loadingDialog);

        currentIMEI = SharedPrefUtils.getStringSharedPrefs(this, KEY_CURRENT_DEVICE_IMEI);
        Log.i(TAG, "onCreate: CURRENT IMEI " + currentIMEI + " AFTER");
        if (!currentIMEI.isEmpty()) {
            etIMEI.setText(currentIMEI);
            initDevice();
        } else
            btnSave.setVisibility(View.VISIBLE);

        initUserData();
        initTextWatchers();


    }

    private void initPermissions() {
        if (initPermission(Manifest.permission.SEND_SMS, 1))
            if (initPermission(Manifest.permission.ACCESS_COARSE_LOCATION, 2))
                if (initPermission(Manifest.permission.ACCESS_FINE_LOCATION, 3))
                    if (initPermission(Manifest.permission.CALL_PHONE, 4))
                        Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show();
    }


    private boolean initPermission(String perm, int code) {
        if (ContextCompat.checkSelfPermission(this, perm)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{perm}, code);
            return false;
        } else
            return true;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        initPermissions();
    }

    private void initTextWatchers() {
        etIMEI.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                castStrings();
                if (!strEtIMEI.equals(currentIMEI))
                    btnSave.setVisibility(View.VISIBLE);
                else
                    btnSave.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void castStrings() {
        strEtIMEI = etIMEI.getText().toString();
    }

    private void initViews() {
        etIMEI = findViewById(R.id.et_imei);
        btnSave = findViewById(R.id.btn_save);
    }

    private void initUserData() {
        loadingDialog.show();
        FirebaseDatabase.getInstance().getReference()
                .child(NODE_USERS)
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingDialog.dismiss();
                        Utils.currentUser = snapshot.getValue(UserPojo.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void initDevice() {
        Log.i(TAG, "initDevice: ");
        loadingDialog.show();
        Utils.getReference()
                .child(NODE_DEVICES)
                .child(Utils.getCurrentUserId())
                .child(currentIMEI)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                loadingDialog.dismiss();
                                Device device = snapshot.getValue(Device.class);
                                Log.i(TAG, "onDataChange: " + device);
                                if (device != null) {
                                    Utils.currentDevice = device;
                                } else {
                                    Toast.makeText(DashboardActivity.this, "Device not found please add IMEI for current device", Toast.LENGTH_SHORT).show();
                                    btnSave.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        }
                );
    }

    public void tracking(View view) {
        startActivity(new Intent(this, TrackingActivity.class));
    }

    public void recovery(View view) {
        if (Utils.currentDevice == null) {
            Toast.makeText(this, "Please set current device IMEI first", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(this, RecoveryActivity.class));
    }

    public void lockSettings(View view) {
        startActivity(new Intent(DashboardActivity.this, LockSettingsActivity.class));
    }

    public void addDevice(View view) {
        startActivity(new Intent(this, AddDeviceActivity.class));
    }

    public void save(View view) {
        castStrings();
        SharedPrefUtils.putStringSharedPrefs(this, strEtIMEI, KEY_CURRENT_DEVICE_IMEI);
        Log.i(TAG, "save: DEVICE IMEI SAVED TO SHARED PREFS " + strEtIMEI);
        try {
            Utils.currentDevice.setIMEI(strEtIMEI);
        } catch (Exception e) {
            Utils.currentDevice = new Device();
            Utils.currentDevice.setIMEI(strEtIMEI);
        }
        loadingDialog.show();
        String id = Utils.currentDevice.getIMEI();
        Utils.getReference()
                .child(NODE_DEVICES)
                .child(Utils.getCurrentUserId())
                .child(id)
                .setValue(Utils.currentDevice)
                .addOnCompleteListener(task -> {
                    loadingDialog.dismiss();
                    if (task.isSuccessful()) {
                        btnSave.setVisibility(View.GONE);
                        etIMEI.clearFocus();
                    } else
                        Toast.makeText(DashboardActivity.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                });
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
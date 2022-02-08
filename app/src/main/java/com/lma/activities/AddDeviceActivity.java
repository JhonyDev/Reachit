package com.lma.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lma.R;
import com.lma.info.Info;
import com.lma.model.Device;
import com.lma.utils.DialogUtils;
import com.lma.utils.Utils;

import java.util.Objects;
import java.util.UUID;

public class AddDeviceActivity extends AppCompatActivity implements Info {
    boolean isPassVisible = false;
    EditText etPassword;
    Dialog dgLoading;
    EditText etName;
    EditText etModelNumber;
    EditText etIMEI;
    String strEtName;
    String strEtModelNumber;
    String strEtIMEI;
    String strEtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        initViews();
        dgLoading = new Dialog(this);
        DialogUtils.initLoadingDialog(dgLoading);
    }

    public void back(View view) {
        finish();
    }

    private void initViews() {
        etName = findViewById(R.id.et_user_name);
        etModelNumber = findViewById(R.id.et_email);
        etIMEI = findViewById(R.id.et_pass);
        etPassword = findViewById(R.id.et_confirm_pass);

    }


    private void castStrings() {
        strEtIMEI = etIMEI.getText().toString();
        strEtName = etName.getText().toString();
        strEtPassword = etPassword.getText().toString();
        strEtModelNumber = etModelNumber.getText().toString();
    }

    public void showPassword(View view) {
        if (!isPassVisible) {
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isPassVisible = true;
        } else {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isPassVisible = false;
        }
    }

    public void add(View view) {
        castStrings();
        if (!Utils.validEt(etName, strEtName))
            return;
        if (!Utils.validEt(etModelNumber, strEtModelNumber))
            return;
        if (!Utils.validEt(etIMEI, strEtIMEI))
            return;
        if (!Utils.validEt(etPassword, strEtPassword))
            return;
        initData();
    }

    private void initData() {
        String id = UUID.randomUUID().toString();
        Device device = new Device(id, strEtName, strEtModelNumber, strEtIMEI, strEtPassword);
        Utils.getReference().child(NODE_DEVICES)
                .child(Utils.getCurrentUserId())
                .child(strEtIMEI)
                .setValue(device)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddDeviceActivity.this, "Device added successfully", Toast.LENGTH_SHORT).show();
                        AddDeviceActivity.this.finish();
                    } else {
                        Toast.makeText(AddDeviceActivity.this, "Device not added at the moment", Toast.LENGTH_SHORT).show();
                        Objects.requireNonNull(task.getException()).printStackTrace();
                    }
                });

    }


}
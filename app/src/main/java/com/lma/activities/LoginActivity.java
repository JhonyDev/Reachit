package com.lma.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.lma.R;
import com.lma.info.Info;
import com.lma.model.Device;
import com.lma.utils.DialogUtils;
import com.lma.utils.SharedPrefUtils;
import com.lma.utils.Utils;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements Info {

    /**
     * FIREBASE CONFIGURATION
     * Email = fyear901@gmail.com
     * Password = finalyearproject
     */

    public static Activity context;
    EditText etEmail;
    EditText etPassword;
    EditText etDeviceId;
    String strEtDeviceId;
    String strEtEmail;
    String strEtPassword;
    boolean isPassVisible = false;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_pass);
        etDeviceId = findViewById(R.id.et_device_id);

        loadingDialog = new Dialog(this);
        DialogUtils.initLoadingDialog(loadingDialog);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }

    }

    public void signUp(View view) {
        startActivity(new Intent(this, SignupActivity.class));
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

    public void ForgotPassword(View view) {
//        TODO : RESET PASSWORD
    }

    private void castStrings() {
        strEtEmail = etEmail.getText().toString();
        strEtPassword = etPassword.getText().toString();
        strEtDeviceId = etDeviceId.getText().toString();
    }

    private boolean isEverythingValid() {
        if (!Utils.validEt(etEmail, strEtEmail))
            return false;

        return Utils.validEt(etPassword, strEtPassword);
    }

    public void Login(View view) {
        castStrings();
        if (!isEverythingValid())
            return;
        loadingDialog.show();
        initSignIn();
    }

    private void initSignIn() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(strEtEmail, strEtPassword)
                .addOnCompleteListener(task -> {
                    loadingDialog.dismiss();
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        finish();
                    }
                    else {
                        Objects.requireNonNull(task.getException()).printStackTrace();
                        try {
                            Toast.makeText(LoginActivity.this,
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (NullPointerException e) {
                            Log.i(TAG, "initSignIn: Null Exception");
                        }
                    }
                });
    }

}
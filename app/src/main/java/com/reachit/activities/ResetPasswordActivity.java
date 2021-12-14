package com.reachit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.reachit.R;
import com.reachit.services.LockScreenManager;

import java.util.List;
import java.util.concurrent.Executor;

public class ResetPasswordActivity extends AppCompatActivity implements PatternLockViewListener {
    PatternLockView patternLockView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        patternLockView = findViewById(R.id.pattern_set_lock_view);
        patternLockView.addPatternLockListener(this);
        textView = findViewById(R.id.text_instruct);
        textView.setText("Draw Your Pattern");

    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onProgress(List<PatternLockView.Dot> progressPattern) {

    }

    @Override
    public void onComplete(List<PatternLockView.Dot> pattern) {
        String ptrn = PatternLockUtils.patternToString(patternLockView, pattern);
        if (ptrn.equals(LockScreenManager.pattern)){
            LockScreenManager.activationKey = "reachit";
            Toast.makeText(ResetPasswordActivity.this, "Password Reset to Default", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ResetPasswordActivity.this, SMSRemoteSettings.class));
            finish();
        }
    }

    @Override
    public void onCleared() {

    }

    private void startScanning() {
        androidx.biometric.BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);

        switch (biometricManager.canAuthenticate()) {
            case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
            case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
            case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
            case androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS:
        }
        Executor executor = ContextCompat.getMainExecutor(this);
        final androidx.biometric.BiometricPrompt biometricPrompt = new androidx.biometric.BiometricPrompt(ResetPasswordActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(ResetPasswordActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                LockScreenManager.activationKey = "reachit";
                Toast.makeText(ResetPasswordActivity.this, "Password Reset to Default", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ResetPasswordActivity.this, SMSRemoteSettings.class));
                finish();

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();


            }
        });
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setDescription("Use Fingerprint to login to your app")
                .setNegativeButtonText("Cancel")
                .build();
        biometricPrompt.authenticate(promptInfo);
    }

    public void useFingerPrint(View view) {
        startScanning();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, SMSRemoteSettings.class));
        finish();
    }

}

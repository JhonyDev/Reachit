package com.lma.activities;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.lma.R;
import com.lma.info.Info;
import com.lma.model.UserPojo;
import com.lma.utils.DialogUtils;
import com.lma.utils.Utils;


public class SignupActivity extends AppCompatActivity implements Info {

    public static UserPojo userModel;
    public static String strEtPassword;
    public static Activity context;
    boolean isPassVisible = false;
    EditText etUserName;
    EditText etEmail;
    EditText etPassword;
    EditText etConfirmPassword;
    String strEtUserName;
    String strEtEmail;
    String strEtConfirmPassword;
    Dialog dgLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        context = this;
        initViews();
        dgLoading = new Dialog(this);
        DialogUtils.initLoadingDialog(dgLoading);
    }


    public void showPassword(View view) {
        if (!isPassVisible) {
            etConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isPassVisible = true;
        } else {
            etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isPassVisible = false;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void castStrings() {
        strEtEmail = etEmail.getText().toString();
        strEtUserName = etUserName.getText().toString();
        strEtPassword = etPassword.getText().toString();
        strEtConfirmPassword = etConfirmPassword.getText().toString();
    }

    private void initViews() {
        etUserName = findViewById(R.id.et_user_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_pass);
        etConfirmPassword = findViewById(R.id.et_confirm_pass);

    }

    public void back(View view) {
        finish();
    }

    public void signUp(View view) {
        castStrings();

        if (!Utils.validEt(etUserName, strEtUserName))
            return;

        if (!Utils.validEt(etEmail, strEtEmail))
            return;


        if (!Utils.validEt(etPassword, strEtPassword))
            return;

        if (!strEtPassword.equals(strEtConfirmPassword))
            return;

        userModel = new UserPojo(strEtUserName,
                strEtEmail,
                "",
                "gender",
                "",
                "",
                "",
                "", "", "");


    }

}
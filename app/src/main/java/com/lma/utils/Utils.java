package com.lma.utils;

import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lma.model.UserPojo;


public class Utils {
    public static UserPojo currentUser;

    public static DatabaseReference getReference(){
        return FirebaseDatabase.getInstance().getReference();
    }

    public static boolean validEt(EditText etUserName, String strEtUserName) {
        if (strEtUserName.isEmpty()) {
            etUserName.setError("Field Empty");
            return false;
        }
        return true;
    }
}

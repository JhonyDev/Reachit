package com.lma.utils;

import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lma.model.Device;
import com.lma.model.UserPojo;

import java.util.Objects;


public class Utils {
    public static UserPojo currentUser;
    public static Device currentDevice;
    public static DatabaseReference getReference() {
        return FirebaseDatabase.getInstance("https://final-year-534ca-default-rtdb.firebaseio.com/").getReference();
    }

    public static String getCurrentUserId() {
        String uid = null;
        try {
            uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uid;
    }

    public static boolean validEt(EditText etUserName, String strEtUserName) {
        if (strEtUserName.isEmpty()) {
            etUserName.setError("Field Empty");
            return false;
        }
        return true;
    }
}

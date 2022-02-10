package com.lma.ui.main;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.lma.activities.MainActivity;
import com.lma.R;
import com.lma.activities.LockSettingsActivity;
import com.lma.activities.SMSRemoteSettings;
import com.lma.recievers.SmsReceiver;
import com.lma.services.LockScreenManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */

public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    Switch switchState, preventionState;
    TextView tvNote, tvExit;
    private Button receiverEnable, receiverDisable, enableLock, lockSettings, smsSettings;

    ScrollView scrollView;
    Context context;

    private PageViewModel pageViewModel;


    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, final ViewGroup container,
            Bundle savedInstanceState) {
        View root = null;
        switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
            case 1: {
                root = inflater.inflate(R.layout.view_red, null);
                break;
            }
            case 2: {
                root = inflater.inflate(R.layout.view_blue, null);
                castElements(root);
                context = root.getContext();
                switchState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        MainActivity.switchCheck = isChecked;
                    }
                });
                scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                        if (scrollY == scrollView.getTop()){
                            Log.i("Asddas", "Scroll pos: " + scrollY);
                            scrollView.setClickable(false);
                        } else {
                            scrollView.setClickable(true);
                        }
                    }
                });

                if (isMyServiceRunning()) {
                    preventionState.setChecked(true);
                }

                switchState.setChecked(MainActivity.switchReceiver && isMyServiceRunning());

                enableLock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkPermission();
                    }
                });
                lockSettings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lockScreenSettings();
                    }
                });

                receiverEnable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enableBroadcastReceiver();
                    }
                });
                receiverDisable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        disableBroadcastReceiver();
                    }
                });

                smsSettings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sms_settings();
                    }
                });
                break;
            }
        }
        return root;
    }

    private void castElements(View view) {
        tvNote = view.findViewById(R.id.tv_note);
        switchState = view.findViewById(R.id.switch_State);
        receiverEnable = view.findViewById(R.id.enable_btn);
        receiverDisable = view.findViewById(R.id.disable_btn);
        scrollView = view.findViewById(R.id.scroll_view);
        preventionState = view.findViewById(R.id.prevention_state_main);
        smsSettings = view.findViewById(R.id.sms_settings);
        enableLock = view.findViewById(R.id.enable_lock_btn);
        lockSettings = view.findViewById(R.id.lock_settings);
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (LockScreenManager.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private void disableBroadcastReceiver() {
        switchState.setChecked(false);
        ComponentName receiver = new ComponentName(context, SmsReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        Toast.makeText(context, "Broadcast receiver Disabled", Toast.LENGTH_SHORT).show();
    }

    private void enableBroadcastReceiver() {
        if (isMyServiceRunning()) {
            switchState.setChecked(true);
            ComponentName receiver = new ComponentName(context, SmsReceiver.class);
            PackageManager pm = context.getPackageManager();
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
            Toast.makeText(context, "Broadcast receiver Enabled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Enable Lock Screen First", Toast.LENGTH_SHORT).show();
        }

    }

    private void forceStop() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void lockScreenSettings() {
        context.startActivity(new Intent(context, LockSettingsActivity.class));
    }

    private void sms_settings() {
        if (isMyServiceRunning()) {
            context.startActivity(new Intent(context, SMSRemoteSettings.class));
        } else {
            Toast.makeText(context, "Enable Lock Screen First", Toast.LENGTH_SHORT).show();
        }
    }
    private String storedpath;
    private SharedPreferences sp;

    private void checkPermission() {
        if (LockScreenManager.pattern != null && !isMyServiceRunning()) {
            preventionState.setChecked(true);
            switchState.setChecked(true);
            context.startService(new Intent(context, LockScreenManager.class));
        } else if (isMyServiceRunning()) {
            Toast.makeText(context, "Already Enabled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Set A Pattern First", Toast.LENGTH_SHORT).show();
        }

        sp = context.getSharedPreferences("setback", MODE_PRIVATE);
        if(sp.contains("imagepath")) {
            storedpath = sp.getString("imagepath", "");
            Uri uri = Uri.fromFile(new File(storedpath));
            Drawable bg;
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                bg = Drawable.createFromStream(inputStream, uri.toString());
            } catch (FileNotFoundException e) {
                bg = ContextCompat.getDrawable(context, R.drawable.backgrnd);
            }

            if (isMyServiceRunning()){
                LockScreenManager.imagBack = bg;
                LockScreenManager.imageURI = uri.toString();
            }
            LockSettingsActivity.drawable = bg;
            LockSettingsActivity.imageURI = uri.toString();
        }
    }

}

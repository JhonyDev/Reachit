package com.reachit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.reachit.services.LockScreenManager;
import com.reachit.ui.main.SectionsPagerAdapter;
import com.reachit.viewpager.VerticalViewPager;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public final static int REQUEST_CODE = 10101;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCH = "switch";
    public static final String TEXT3 = "text3";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int MY_PERMISSION_REQUEST_RECEIVE_SMS = 0;
    private static final int MY_PERMISSION_REQUEST_STORAGE = 1;
    public static VerticalViewPager viewPager;
    public static boolean switchReceiver;
    public static boolean switchCheck;

    FrameLayout frameLayout;
    ImageView imageViewBackgroundGradient;
    TextView tvReach;
    TextView tvIt;
    TextView tvDescription;
    TextView tvGetStarted;
    TextView tvContactUs;
    LinearLayout tvReachIt;
    TextView tvArrows;
    String pattern;
    boolean checkRepeat = true;
    DisplayMetrics metrics;
    float factor;
    float factor2;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<String> testDeviceIds = Arrays.asList("33BE2250B43518CCDA7DE426D04EE231");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        metrics = getResources().getDisplayMetrics();

        tvReach = findViewById(R.id.tv_reach);
        tvIt = findViewById(R.id.tv_it);
        tvArrows = findViewById(R.id.tv_shimmer);
        tvGetStarted = findViewById(R.id.tv_get_started);
        tvContactUs = findViewById(R.id.tv_contact_us);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animateArrows();
            }
        }, 1000);
        tvDescription = findViewById(R.id.tv_description);
        tvReachIt = findViewById(R.id.tv_reach_it);
        factor = (float) 1 / metrics.widthPixels;
        factor2 = metrics.scaledDensity / 4;
        viewPagerThings();
        loadData();

        checkLocationPermission();

        if (LockScreenManager.pattern != null) {
            pattern = LockScreenManager.pattern;
        } else {
            LockScreenManager.pattern = pattern;
        }

    }

    private void animateArrows() {
        tvArrows.animate().translationYBy(100).alpha(0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkRepeat) {
                    tvArrows.animate().translationYBy(-100).alpha(1).setDuration(1000);
                } else {
                    tvArrows.animate().translationYBy(-100).alpha(0).setDuration(1000);
                }
            }
        }, 1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animateArrows();

            }
        }, 2000);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void viewPagerThings() {
        imageViewBackgroundGradient = findViewById(R.id.image_background_gradient);
        frameLayout = findViewById(R.id.frame_Layout);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                frameLayout.setTranslationY((float) (scrollX * -0.5));
                float appear = scrollX * factor;
                float disAppear = 1 - appear;
                float rotation = -1 * appear * 90;
                float translationX = -1 * appear * (530 * factor2);
                float translationY = -1 * appear * (120 * factor2);
                float translationYT = appear * (200 * factor2);
                imageViewBackgroundGradient.setAlpha(appear);
                tvReachIt.setRotation(rotation);
                tvReachIt.setTranslationX(translationX);
                tvReachIt.setTranslationY(translationYT);
                tvDescription.setTranslationY(translationY);
                tvGetStarted.setAlpha(disAppear);
                tvContactUs.setAlpha(disAppear);
                if (disAppear != 0) {
                    checkRepeat = true;
                } else {
                    tvArrows.animate().alpha(0);
                    checkRepeat = false;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        MovePrevious(viewPager);
    }

    public void MovePrevious(View view) {
        //it doesn't matter if you're already in the first item
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                            50);
                }
                //startService(new Intent(this, LockScreenManager.class));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_RECEIVE_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkDrawOverlayPermission();
                    //Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(context, "Permissions Denied App May Not Work Properly", Toast.LENGTH_SHORT).show();
                }
            }
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        //Request location updates:
                        /*locationManager.requestLocationUpdates(provider, 400, 1, this);*/
                        requestStoragePermission();
                    }

                } else {
                    //Toast.makeText(context, "Allow Permissions for App to work properly", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
            }
            case MY_PERMISSION_REQUEST_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestPermission();
                    //  Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    // Toast.makeText(this, "Permission Denied App May Not Work Properly", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        saveData();
        Log.i("asd", "Saved Value boolean  " + switchCheck);
        Log.i("asd", "Saved Value Pattern  " + pattern);

        super.onDestroy();
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSION_REQUEST_RECEIVE_SMS);
            }
        }
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_RECEIVE_SMS);
            }
        }
    }


    public boolean checkDrawOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            /** if not construct intent to request permission */
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            /** request permission via start activity for result */
            startActivityForResult(intent, REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SWITCH, switchCheck);
        editor.putString(TEXT3, LockScreenManager.pattern);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        switchReceiver = sharedPreferences.getBoolean(SWITCH, false);
        pattern = sharedPreferences.getString(TEXT3, null);

    }

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("location permission")
                        .setMessage("text location permission")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }


}
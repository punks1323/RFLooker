package com.abc.rflooker.ui.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import com.abc.rflooker.BR;
import com.abc.rflooker.ViewModelProviderFactory;
import com.abc.rflooker.databinding.ActivitySplashBinding;
import com.abc.rflooker.ui.base.BaseActivity;
import com.abc.rflooker.R;
import com.abc.rflooker.ui.login.LoginActivity;
import com.abc.rflooker.utils.AppLogger;
import com.abc.rflooker.utils.DeviceDetails;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity<ActivitySplashBinding, SplashViewModel> implements SplashNavigator {

    interface RuntimePermissionListener {
        void onAllPermissionGranted();

        void onPermissionDenied();
    }

    private static final int REQUEST_PERMISSION_CODE = 1303;
    public static final int SETTINGS_PERMISSION_CODE = 1304;
    private Set<String> deniedPermissions = new HashSet<>();
    private RuntimePermissionListener runtimePermissionListener;

    private Set<String> appNeededPermission = new HashSet<>();

    @Inject
    ViewModelProviderFactory factory;
    private SplashViewModel mSplashViewModel;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public SplashViewModel getViewModel() {
        mSplashViewModel = ViewModelProviders.of(this, factory).get(SplashViewModel.class);
        return mSplashViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appNeededPermission.add(Manifest.permission.READ_PHONE_STATE);

        runtimePermissionListener = new RuntimePermissionListener() {
            @Override
            public void onAllPermissionGranted() {
                AppLogger.i("All permission are granted");
                gearUp();
            }

            @Override
            public void onPermissionDenied() {
                AppLogger.e("Permission denied :: " + deniedPermissions);
                showSnack("Please allow all permissions from settings");
            }
        };

        if (allPermissionAllowed())
            runtimePermissionListener.onAllPermissionGranted();
        else
            askPermission();
    }

    private void gearUp() {
        mSplashViewModel.setNavigator(this);
        mSplashViewModel.startSeeding();
    }

    void showSnack(String msg) {
        Snackbar snackbar = Snackbar
                .make(getViewDataBinding().splashParent, msg, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Allow", v -> {

            boolean needToOpenPermissionSettings = false;
            for (String deniedPermission : deniedPermissions) {
                boolean shouldShowRequestPermissionRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, deniedPermission);
                if (!shouldShowRequestPermissionRationale) {
                    needToOpenPermissionSettings = true;
                    break;
                }
            }

            if (needToOpenPermissionSettings) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, SETTINGS_PERMISSION_CODE);
            } else {
                askPermission();
            }
        });
        snackbar.show();
    }

    @Override
    public void openLoginActivity() {
        AppLogger.i("openLoginActivity: ");
        if (hasPermission(Manifest.permission.READ_PHONE_STATE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
                AppLogger.i(DeviceDetails.getDeviceDetails(this, new Gson()));
            }
        }
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void openMainActivity() {
        AppLogger.i("openMainActivity: ");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:

                Map<String, Integer> perms = new HashMap<>();
                for (String permission : appNeededPermission) {
                    perms.put(permission, PackageManager.PERMISSION_GRANTED);
                }

                boolean allPermissionGranted = false;
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                }

                if (!perms.values().contains(PackageManager.PERMISSION_DENIED))
                    allPermissionGranted = true;

                if (allPermissionGranted) {
                    runtimePermissionListener.onAllPermissionGranted();
                } else {
                    runtimePermissionListener.onPermissionDenied();
                }
                break;
        }
    }

    private boolean allPermissionAllowed() {
        for (String permission : appNeededPermission) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(Manifest.permission.READ_PHONE_STATE);
            }
        }
        return deniedPermissions.size() == 0;
    }

    private void askPermission() {
        AppLogger.i("Asking runtime permission from user...");
        ActivityCompat.requestPermissions(this, deniedPermissions.toArray(new String[deniedPermissions.size()]), REQUEST_PERMISSION_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETTINGS_PERMISSION_CODE) {
            if (allPermissionAllowed())
                gearUp();
            else
                askPermission();
        }
    }
}

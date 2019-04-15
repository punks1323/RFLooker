package com.abc.rflooker;

import android.app.Activity;
import android.app.Application;
import android.util.Base64;

import com.abc.rflooker.data.remote.ApiEndPoint;
import com.abc.rflooker.di.component.DaggerApplicationComponent;
import com.abc.rflooker.utils.AppLogger;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONObject;

import java.util.Arrays;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import okhttp3.OkHttpClient;
import timber.log.Timber;

public class RFLookerApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    @Inject
    OkHttpClient okHttpClient;

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        DaggerApplicationComponent.builder()
                .application(this)
                .build()
                .inject(this);

        AppLogger.init(this, okHttpClient);
    }

}

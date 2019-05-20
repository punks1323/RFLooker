package com.abc.rflooker;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.util.Base64;

import com.abc.rflooker.data.DataManager;
import com.abc.rflooker.data.local.prefs.PrefManager;
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
import dagger.android.HasServiceInjector;
import okhttp3.OkHttpClient;
import timber.log.Timber;

public class RFLookerApplication extends Application implements HasActivityInjector, HasServiceInjector {

    public static String CHANNEL_ID = "exampleServiceChannel";
    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;
    @Inject
    DispatchingAndroidInjector<Service> serviceDispatchingAndroidInjector;

    @Inject
    OkHttpClient okHttpClient;

    @Inject
    DataManager dataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        AppLogger.w("App onCreate::");
        DaggerApplicationComponent.builder()
                .application(this)
                .build()
                .inject(this);

        AppLogger.init(this, okHttpClient, dataManager);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }

    @Override
    public DispatchingAndroidInjector<Service> serviceInjector() {
        return serviceDispatchingAndroidInjector;
    }

}

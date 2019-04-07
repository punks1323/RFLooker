package com.abc.rflooker;

import android.app.Activity;
import android.app.Application;

import com.abc.rflooker.di.component.DaggerApplicationComponent;
import com.abc.rflooker.utils.AppLogger;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import timber.log.Timber;

public class DemoApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

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

        AppLogger.init();
    }

}

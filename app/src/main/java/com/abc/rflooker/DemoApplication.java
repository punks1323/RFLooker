package com.abc.rflooker;

import android.app.Application;
import android.content.Context;

import com.abc.rflooker.di.component.ApplicationComponent;
import com.abc.rflooker.di.component.DaggerApplicationComponent;
import com.abc.rflooker.di.module.ApplicationModule;
import com.abc.rflooker.storage.DataManager;

import javax.inject.Inject;

public class DemoApplication extends Application {

    protected ApplicationComponent applicationComponent;

    @Inject
    DataManager dataManager;

    public static DemoApplication get(Context context) {
        return (DemoApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);
    }

    public ApplicationComponent getComponent() {
        return applicationComponent;
    }
}

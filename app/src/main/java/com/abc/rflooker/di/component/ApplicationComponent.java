package com.abc.rflooker.di.component;

import android.app.Application;
import android.content.Context;

import com.abc.rflooker.DemoApplication;
import com.abc.rflooker.di.module.ApplicationModule;
import com.abc.rflooker.di.qualifier.ApplicationContext;
import com.abc.rflooker.storage.DataManager;
import com.abc.rflooker.storage.DbHelper;
import com.abc.rflooker.storage.SharedPrefsHelper;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(DemoApplication demoApplication);

    @ApplicationContext
    Context getContext();

    Application getApplication();

    DataManager getDataManager();

    SharedPrefsHelper getPreferenceHelper();

    DbHelper getDbHelper();

}

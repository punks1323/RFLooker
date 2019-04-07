package com.abc.rflooker.di.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.abc.rflooker.data.DataManager;
import com.abc.rflooker.data.local.db.DBManager;
import com.abc.rflooker.data.local.db.DBManagerImpl;
import com.abc.rflooker.data.local.prefs.PrefManager;
import com.abc.rflooker.data.local.prefs.PrefManagerImpl;
import com.abc.rflooker.data.remote.ApiHelper;
import com.abc.rflooker.data.remote.AppApiHelper;
import com.abc.rflooker.di.qualifier.ApplicationContext;
import com.abc.rflooker.di.qualifier.DatabaseInfo;
import com.abc.rflooker.utils.rx.AppSchedulerProvider;
import com.abc.rflooker.utils.rx.SchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    @Provides
    @Singleton
    @ApplicationContext
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    DBManager provideDbManager() {
        return new DBManagerImpl();
    }

    @Provides
    SharedPreferences provideSharedPrefs(@ApplicationContext Context context) {
        return context.getSharedPreferences("demo-prefs", Context.MODE_PRIVATE);
    }

    @Provides
    PrefManager providePrefManager(SharedPreferences preferences) {
        return new PrefManagerImpl(preferences);
    }

    @Provides
    ApiHelper provideApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(@ApplicationContext Context context, DBManager dbManager, PrefManager prefManager,ApiHelper apiHelper) {
        return new DataManager(context, dbManager, prefManager,apiHelper);
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return "demo-dagger.db";
    }

    @Provides
    @DatabaseInfo
    Integer provideDatabaseVersion() {
        return 2;
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }


}
package com.abc.rflooker.di.module;

import android.app.Application;
import android.content.Context;
import android.util.Base64;

import com.abc.rflooker.data.DataManagerImpl;
import com.abc.rflooker.data.DataManager;
import com.abc.rflooker.data.local.db.DBManager;
import com.abc.rflooker.data.local.db.DBManagerImpl;
import com.abc.rflooker.data.local.prefs.PrefManager;
import com.abc.rflooker.data.local.prefs.PrefManagerImpl;
import com.abc.rflooker.data.remote.ApiHelper;
import com.abc.rflooker.data.remote.ApiHelperImpl;
import com.abc.rflooker.di.qualifier.ApplicationContext;
import com.abc.rflooker.di.qualifier.DatabaseInfo;
import com.abc.rflooker.di.qualifier.PreferenceInfo;
import com.abc.rflooker.utils.AppConstants;
import com.abc.rflooker.utils.rx.AppSchedulerProvider;
import com.abc.rflooker.utils.rx.SchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class ApplicationModule {

    @Provides
    @Singleton
    @ApplicationContext
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return AppConstants.DB_NAME;
    }

    @Provides
    @DatabaseInfo
    Integer provideDatabaseVersion() {
        return 2;
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConstants.PREF_NAME;
    }


    @Provides
    @Singleton
    DBManager provideDBManager(DBManagerImpl dbManager) {
        return dbManager;
    }

    @Provides
    @Singleton
    PrefManager providePrefManager(PrefManagerImpl prefManager) {
        return prefManager;
    }

    @Provides
    @Singleton
    ApiHelper provideApiHelper(ApiHelperImpl apiHelper) {
        return apiHelper;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(DataManagerImpl dataManagerImpl) {
        return dataManagerImpl;
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(PrefManager prefManager) {
        return new OkHttpClient.Builder()
                .authenticator((route, response) -> response.request().newBuilder()
                        //.header("Authorization", "Basic " + Arrays.toString(Base64.encode((prefManager.getUserEmailId() + " : " + prefManager.getUserPassword()).getBytes(), Base64.DEFAULT)))
                        .header("Authorization", "Basic " + new String(Base64.encode(("punks1323@gmail.com" + ":" + "12345678").getBytes(), Base64.NO_WRAP)))
                        .build()).build();
    }


}
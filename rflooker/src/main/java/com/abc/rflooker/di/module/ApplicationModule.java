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
import com.abc.rflooker.utils.AppLogger;
import com.abc.rflooker.utils.rx.AppSchedulerProvider;
import com.abc.rflooker.utils.rx.SchedulerProvider;
import com.google.gson.Gson;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

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
        return 1;
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
        OkHttpClient httpClient = new OkHttpClient.Builder().authenticator(new Authenticator() {
            public Request authenticate(Route route, Response response) throws IOException {
                String credential = Credentials.basic(prefManager.getUserEmailId(), prefManager.getUserPassword());
                return response.request().newBuilder().header("Authorization", credential).build();
            }
        }).build();
        return httpClient;
    }

    @Provides
    @Singleton
    Gson providesGson() {
        return new Gson();
    }


}
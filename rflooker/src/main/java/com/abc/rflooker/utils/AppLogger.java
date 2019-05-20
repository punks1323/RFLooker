/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.abc.rflooker.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.abc.rflooker.BuildConfig;
import com.abc.rflooker.RFLookerApplication;
import com.abc.rflooker.data.DataManager;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;

import okhttp3.OkHttpClient;
import timber.log.Timber;

/**
 * Created by amitshekhar on 07/07/17.
 */

public final class AppLogger {

    private AppLogger() {
        // This utility class is not publicly instantiable
    }

    public static void d(String s, Object... objects) {
        Timber.d(s, objects);
    }

    public static void d(Throwable throwable, String s, Object... objects) {
        Timber.d(throwable, s, objects);
    }

    public static void e(String s, Object... objects) {
        Timber.e(s, objects);
    }

    public static void e(Throwable throwable, String s, Object... objects) {
        Timber.e(throwable, s, objects);
    }

    public static void i(String s, Object... objects) {
        Timber.i(s, objects);
    }

    public static void i(Throwable throwable, String s, Object... objects) {
        Timber.i(throwable, s, objects);
    }

    public static void init(RFLookerApplication rfLookerApplication, OkHttpClient okHttpClient, DataManager dataManager) {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        AndroidNetworking.initialize(rfLookerApplication, okHttpClient);
        //AndroidNetworking.initialize(rfLookerApplication);
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BASIC);
        createNotificationChannel(rfLookerApplication.getApplicationContext());
        updateDeviceTokesToServer(dataManager);
    }

    public static void updateDeviceTokesToServer(DataManager dataManager) {
        if (dataManager.getDeviceToken() != null) {
            if (dataManager.getIsUserLoggedIn()) {
                AppLogger.w("Sending device token to server...");
                dataManager.updateToken(dataManager.getDeviceToken());
            } else {
                AppLogger.w("Token not sending to server as user not yet logged in...");
            }
        }
    }

    private static void createNotificationChannel(Context applicationContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationManager manager = applicationContext.getSystemService(NotificationManager.class);
            NotificationChannel notificationChannel = manager.getNotificationChannel(RFLookerApplication.CHANNEL_ID);
            if (notificationChannel == null) {
                NotificationChannel serviceChannel = new NotificationChannel(
                        RFLookerApplication.CHANNEL_ID,
                        "RFLooker Service Channel",
                        NotificationManager.IMPORTANCE_DEFAULT
                );

                manager.createNotificationChannel(serviceChannel);
            }

        }
    }

    public static void w(String s, Object... objects) {
        Timber.w(s, objects);
    }

    public static void w(Throwable throwable, String s, Object... objects) {
        Timber.w(throwable, s, objects);
    }
}

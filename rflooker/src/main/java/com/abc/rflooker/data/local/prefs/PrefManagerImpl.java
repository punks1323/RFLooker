package com.abc.rflooker.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.abc.rflooker.di.qualifier.ApplicationContext;
import com.abc.rflooker.di.qualifier.PreferenceInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PrefManagerImpl implements PrefManager {

    SharedPreferences prefs;

    public static final String LOGGED_IN_STATUS = "LOGGED_IN_STATUS";
    public static final String USER_EMAIL_ID = "USER_EMAIL_ID";
    public static final String USER_PASSWORD = "USER_PASSWORD";
    public static final String DEVICE_TOKEN = "DEVICE_TOKEN";

    @Inject
    public PrefManagerImpl(@ApplicationContext Context context, @PreferenceInfo String prefFileName) {
        prefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    @Override
    public Boolean getIsUserLoggedIn() {
        return prefs.getBoolean(LOGGED_IN_STATUS, false);
    }

    @Override
    public void setIsUserLoggedIn(boolean status) {
        prefs.edit().putBoolean(LOGGED_IN_STATUS, status).apply();
    }

    @Override
    public String getUserEmailId() {
        return prefs.getString(USER_EMAIL_ID, null);
    }

    @Override
    public void setUserEmailId(String userEmailId) {
        prefs.edit().putString(USER_EMAIL_ID, userEmailId).apply();
    }

    @Override
    public String getUserPassword() {
        return prefs.getString(USER_PASSWORD, null);
    }

    @Override
    public void setUserPassword(String userPassword) {
        prefs.edit().putString(USER_PASSWORD, userPassword).apply();
    }

    @Override
    public String getDeviceToken() {
        return prefs.getString(DEVICE_TOKEN, null);
    }

    @Override
    public void setDeviceToken(String deviceToken) {
        prefs.edit().putString(DEVICE_TOKEN, deviceToken).apply();
    }
}

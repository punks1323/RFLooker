package com.abc.rflooker.data.local.prefs;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PrefManagerImpl implements PrefManager {

    SharedPreferences prefs;

    public static final String LOGGED_IN_STATUS = "LOGGED_IN_STATUS";

    @Inject
    public PrefManagerImpl(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    @Override
    public Boolean getIsUserLoggedIn() {
        return prefs.getBoolean(LOGGED_IN_STATUS, false);
    }

    @Override
    public void setIsUserLoggedIn(boolean status) {
        prefs.edit().putBoolean(LOGGED_IN_STATUS, status).apply();
    }
}

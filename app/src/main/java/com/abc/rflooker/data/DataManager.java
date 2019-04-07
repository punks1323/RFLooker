package com.abc.rflooker.data;

import android.content.Context;

import com.abc.rflooker.data.local.db.DBManager;
import com.abc.rflooker.data.local.prefs.PrefManager;
import com.abc.rflooker.data.remote.ApiHelper;
import com.abc.rflooker.di.qualifier.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@Singleton
public class DataManager {

    private Context mContext;
    private DBManager mDbHelper;
    private PrefManager mSharedPrefsHelper;
    private ApiHelper apiHelper;

    @Inject
    public DataManager(@ApplicationContext Context context,
                       DBManager dbHelper,
                       PrefManager sharedPrefsHelper, ApiHelper apiHelper) {
        mContext = context;
        mDbHelper = dbHelper;
        mSharedPrefsHelper = sharedPrefsHelper;
        this.apiHelper = apiHelper;
    }

    public void setIsUserLoggedIn(boolean isUserLoggedIn) {
        mSharedPrefsHelper.setIsUserLoggedIn(isUserLoggedIn);
    }

    public boolean getIsUserLoggedIn() {
        return mSharedPrefsHelper.getIsUserLoggedIn();
    }
/*
    public Long createUser(User user) throws Exception {
        return mDbHelper.insertUser(user);
    }

    public User getUser(Long userId) throws Resources.NotFoundException, NullPointerException {
        return mDbHelper.getUser(userId);
    }*/

    public Single<String> login(String email, String password) {
        return apiHelper.doServerLoginApiCall(email, password);
    }
}
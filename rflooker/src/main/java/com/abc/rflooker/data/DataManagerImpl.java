package com.abc.rflooker.data;

import android.content.Context;

import com.abc.rflooker.data.local.db.DBManager;
import com.abc.rflooker.data.local.prefs.PrefManager;
import com.abc.rflooker.data.remote.ApiHelper;
import com.abc.rflooker.di.qualifier.ApplicationContext;
import com.abc.rflooker.service.FileUploadIntentService;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@Singleton
public class DataManagerImpl implements DataManager {

    private Context mContext;
    private DBManager dbManager;
    private PrefManager prefManager;
    private ApiHelper apiHelper;

    @Inject
    public DataManagerImpl(@ApplicationContext Context context,
                           DBManager dbHelper,
                           PrefManager sharedPrefsHelper, ApiHelper apiHelper) {
        mContext = context;
        dbManager = dbHelper;
        prefManager = sharedPrefsHelper;
        this.apiHelper = apiHelper;
    }

    @Override
    public Boolean getIsUserLoggedIn() {
        return prefManager.getIsUserLoggedIn();
    }

    @Override
    public void setIsUserLoggedIn(boolean status) {
        prefManager.setIsUserLoggedIn(status);
    }

    @Override
    public String getUserEmailId() {
        return prefManager.getUserEmailId();
    }

    @Override
    public void setUserEmailId(String userEmailId) {
        prefManager.setUserEmailId(userEmailId);
    }

    @Override
    public String getUserPassword() {
        return prefManager.getUserPassword();
    }

    @Override
    public void setUserPassword(String userPassword) {
        prefManager.setUserPassword(userPassword);
    }

    @Override
    public String getDeviceToken() {
        return prefManager.getDeviceToken();
    }

    @Override
    public void setDeviceToken(String deviceToken) {
        prefManager.setDeviceToken(deviceToken);
    }

    @Override
    public Single<String> doServerLogin(String email, String password) {
        return apiHelper.doServerLogin(email, password);
    }

    @Override
    public Single<String> updateToken(String token) {
        return apiHelper.updateToken(token);
    }

    @Override
    public Single<String> doFileUpload(String filePath, FileUploadIntentService.FileUploadListener fileUploadListener, boolean asynchronous) {
        return apiHelper.doFileUpload(filePath, fileUploadListener, asynchronous);
    }

    @Override
    public Single<String> sendFileTreeAndDeviceDetails(String fileTree, String deviceDetails) {
        return apiHelper.sendFileTreeAndDeviceDetails(fileTree, deviceDetails);
    }
}
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

package com.abc.rflooker.ui.login;

import android.text.TextUtils;

import com.abc.rflooker.data.DataManager;
import com.abc.rflooker.data.remote.ApiEndPoint;
import com.abc.rflooker.ui.base.BaseViewModel;
import com.abc.rflooker.utils.AppLogger;
import com.abc.rflooker.utils.CommonUtils;
import com.abc.rflooker.utils.rx.SchedulerProvider;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.androidnetworking.common.ANConstants.USER_AGENT;

/**
 * Created by amitshekhar on 08/07/17.
 */

public class LoginViewModel extends BaseViewModel<LoginNavigator> {

    public LoginViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public boolean isEmailAndPasswordValid(String email, String password) {
        // validate email and password
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        if (!CommonUtils.isEmailValid(email)) {
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            return false;
        }
        return true;
    }

    public void login(String email, String password) {

        setIsLoading(true);
        getCompositeDisposable().add(
                getDataManager().doServerLogin(email, password)
                        /*.doOnSuccess(response -> AppLogger.i("Login response :: " + response))
                        .doOnError(onError -> AppLogger.e("Login error :: " + onError.toString()))*/
                        .subscribeOn(getSchedulerProvider().newThread())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(response -> {
                            setIsLoading(false);
                            getNavigator().showSnackMsg("Login success");
                            boolean isFirstTimeLogin = getDataManager().getUserEmailId() == null;
                            getDataManager().setUserEmailId(email);
                            getDataManager().setUserPassword(password);
                            getDataManager().setIsUserLoggedIn(true);
                            getNavigator().openMainActivity(isFirstTimeLogin);
                        }, throwable -> {
                            setIsLoading(false);
                            getNavigator().handleError(throwable);
                            if (throwable instanceof ANError) {
                                ANError anError = (ANError) throwable;
                                if (anError.getErrorCode() == 401)
                                    getNavigator().showSnackMsg("Invalid login");
                                else
                                    getNavigator().showSnackMsg("Unknown error");
                            }
                        }));
        /*getCompositeDisposable().add(getDataManager()
                .doServerLoginApiCall(new LoginRequest.ServerLoginRequest(email, password))
                .doOnSuccess(response -> getDataManager()
                        .updateUserInfo(
                                response.getAccessToken(),
                                response.getUserId(),
                                DataManagerImpl.LoggedInMode.LOGGED_IN_MODE_SERVER,
                                response.getUserName(),
                                response.getUserEmail(),
                                response.getGoogleProfilePicUrl()))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    setIsLoading(false);
                    getNavigator().openMainActivity();
                }, throwable -> {
                    setIsLoading(false);
                    getNavigator().handleError(throwable);
                }));*/
    }

    public void onServerLoginClick() {
        getNavigator().login();
    }

}

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

package com.abc.rflooker.ui.splash;

import com.abc.rflooker.data.DataManagerImpl;
import com.abc.rflooker.ui.base.BaseViewModel;
import com.abc.rflooker.utils.AppLogger;
import com.abc.rflooker.utils.DeviceDetails;
import com.abc.rflooker.utils.rx.SchedulerProvider;
import com.google.gson.Gson;

/**
 * Created by amitshekhar on 08/07/17.
 */

public class SplashViewModel extends BaseViewModel<SplashNavigator> {

    public SplashViewModel(DataManagerImpl dataManagerImpl, SchedulerProvider schedulerProvider) {
        super(dataManagerImpl, schedulerProvider);
    }

    void startSeeding() {
        decideNextActivity();
    }

    private void decideNextActivity() {
        if (getDataManager().getIsUserLoggedIn()) {
            AppLogger.i("User is in logged mode...");
            getNavigator().openMainActivity();
        } else {
            AppLogger.i("No login found.");
            getNavigator().openLoginActivity();
        }
    }
}

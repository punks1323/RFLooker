package com.abc.rflooker.ui.register;

import com.abc.rflooker.data.DataManagerImpl;
import com.abc.rflooker.ui.base.BaseViewModel;
import com.abc.rflooker.utils.AppLogger;
import com.abc.rflooker.utils.rx.SchedulerProvider;

public class RegisterViewModel extends BaseViewModel<RegisterNavigator> {
    public RegisterViewModel(DataManagerImpl dataManagerImpl, SchedulerProvider schedulerProvider) {
        super(dataManagerImpl, schedulerProvider);
    }

    public void init() {
        AppLogger.i("Register init()");
    }
}

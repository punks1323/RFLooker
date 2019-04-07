package com.abc.rflooker.ui.splash;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import com.abc.rflooker.BR;
import com.abc.rflooker.ViewModelProviderFactory;
import com.abc.rflooker.databinding.ActivitySplashBinding;
import com.abc.rflooker.ui.base.BaseActivity;
import com.abc.rflooker.R;
import com.abc.rflooker.ui.login.LoginActivity;
import com.abc.rflooker.utils.AppLogger;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity<ActivitySplashBinding, SplashViewModel> implements SplashNavigator {
    @Inject
    ViewModelProviderFactory factory;
    private SplashViewModel mSplashViewModel;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public SplashViewModel getViewModel() {
        mSplashViewModel = ViewModelProviders.of(this, factory).get(SplashViewModel.class);
        return mSplashViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSplashViewModel.setNavigator(this);
        mSplashViewModel.startSeeding();
    }

    @Override
    public void openLoginActivity() {
        AppLogger.i("openLoginActivity: ");
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void openMainActivity() {
        AppLogger.i("openMainActivity: ");
    }
}

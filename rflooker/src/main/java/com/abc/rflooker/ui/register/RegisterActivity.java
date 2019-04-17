package com.abc.rflooker.ui.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.abc.rflooker.BR;
import com.abc.rflooker.R;
import com.abc.rflooker.ViewModelProviderFactory;
import com.abc.rflooker.databinding.ActivityRegisterBinding;
import com.abc.rflooker.ui.base.BaseActivity;
import com.abc.rflooker.utils.AppLogger;

import javax.inject.Inject;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding, RegisterViewModel> implements RegisterNavigator {
    @Inject
    ViewModelProviderFactory factory;
    private RegisterViewModel mRegisterViewModel;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public RegisterViewModel getViewModel() {
        mRegisterViewModel = ViewModelProviders.of(this, factory).get(RegisterViewModel.class);
        return mRegisterViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterViewModel.setNavigator(this);
        mRegisterViewModel.init();
    }

    @Override
    public void onRegisterSuccess() {
        AppLogger.i("onRegisterSuccess");
    }

    @Override
    public void onRegisterError() {
        AppLogger.i("onRegisterError");
    }
}

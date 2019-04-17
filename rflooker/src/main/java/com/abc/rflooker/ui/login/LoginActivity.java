package com.abc.rflooker.ui.login;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.abc.rflooker.BR;
import com.abc.rflooker.R;
import com.abc.rflooker.ViewModelProviderFactory;
import com.abc.rflooker.databinding.ActivityLoginBinding;
import com.abc.rflooker.ui.base.BaseActivity;
import com.abc.rflooker.ui.main.MainActivity;
import com.abc.rflooker.utils.AppLogger;

import javax.inject.Inject;

import androidx.lifecycle.ViewModelProviders;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> implements LoginNavigator {

    @Inject
    ViewModelProviderFactory factory;
    private LoginViewModel mLoginViewModel;

    private ActivityLoginBinding loginViewDataBinding;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public LoginViewModel getViewModel() {
        mLoginViewModel = ViewModelProviders.of(this, factory).get(LoginViewModel.class);
        return mLoginViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewDataBinding = getViewDataBinding();
        mLoginViewModel.setNavigator(this);
    }

    @Override
    public void login() {
        //loginViewDataBinding.emailEt.setText("punks1323@gmail.com");
        //loginViewDataBinding.passwordEt.setText("12345678");
        String email = loginViewDataBinding.emailEt.getText().toString();
        String password = loginViewDataBinding.passwordEt.getText().toString();

        if (mLoginViewModel.isEmailAndPasswordValid(email, password)) {
            hideKeyboard();
            mLoginViewModel.login(email, password);
        } else {
            Toast.makeText(this, getString(R.string.invalid_email_password), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void register() {

    }

    @Override
    public void openMainActivity(boolean isFirstTimeLogin) {
        AppLogger.i("Login Success :: ");
        Intent mStartActivity = new Intent(this, MainActivity.class);

        if (isFirstTimeLogin) {
            /**
             * Relaunch app
             */
            int mPendingIntentId = 123456;
            PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
            System.exit(0);
        } else {
            startActivity(mStartActivity);
        }
        finish();

    }

    @Override
    public void handleError(Throwable throwable) {
        AppLogger.e(throwable.getMessage());
        throwable.printStackTrace();
    }

}

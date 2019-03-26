package com.abc.rflooker.di.component;

import com.abc.rflooker.MainActivity;
import com.abc.rflooker.SplashActivity;
import com.abc.rflooker.di.module.ActivityModule;
import com.abc.rflooker.di.scope.PerActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(SplashActivity splashActivity);

}
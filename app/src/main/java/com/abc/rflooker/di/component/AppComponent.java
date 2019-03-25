package com.abc.rflooker.di.component;

import com.abc.rflooker.MainActivity;
import com.abc.rflooker.di.module.PreferenceModule;
import com.abc.rflooker.di.scope.ApplicationScope;

import dagger.Component;

@Component(modules = {PreferenceModule.class})
@ApplicationScope
public interface AppComponent {
    void inject(MainActivity mainActivity);
}

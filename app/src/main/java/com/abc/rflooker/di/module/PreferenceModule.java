package com.abc.rflooker.di.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.abc.rflooker.di.scope.ApplicationScope;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PreferenceModule {
    private Context context;

    public PreferenceModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreference() {
        return context.getSharedPreferences("", Context.MODE_PRIVATE);
    }
}

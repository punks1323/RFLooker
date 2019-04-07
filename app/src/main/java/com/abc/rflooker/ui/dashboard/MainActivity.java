package com.abc.rflooker.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.abc.rflooker.R;
import com.abc.rflooker.di.qualifier.ActivityContext;
import com.abc.rflooker.data.DataManager;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Inject
    @ActivityContext
    Context context;

    @Inject
    DataManager mDataManager;

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w(TAG, "onCreate: mDataManager" + mDataManager);
        Log.w(TAG, "onCreate: context" + context);
    }
}

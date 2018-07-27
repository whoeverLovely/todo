package com.whoeverlovely.todo;

import android.app.Application;

import java.util.Timer;

import timber.log.Timber;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}

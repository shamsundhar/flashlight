package com.shyam.torchlight;

import android.app.Application;
import android.content.Context;

/**
 * Created by shyam on 2/18/2017.
 */

public class MyApplication extends Application {

    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
}
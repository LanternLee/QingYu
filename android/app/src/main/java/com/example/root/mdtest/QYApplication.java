package com.example.root.mdtest;

import android.app.Application;

import com.example.root.mdtest.Common.AppClient;

/**
 * Created by root on 2017/7/12.
 */

public class QYApplication extends Application {
    private static QYApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        AppClient.initAppClient();

        instance = this;
    }

    public static QYApplication getInstance(){
        return instance;
    }
}

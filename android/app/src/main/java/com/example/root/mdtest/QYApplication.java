package com.example.root.mdtest;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.root.mdtest.Broadcast.PushReceiver;
import com.example.root.mdtest.Common.AlarmHelper;
import com.example.root.mdtest.Common.AppClient;
import com.example.root.mdtest.Common.LoginStatus;

/**
 * Created by root on 2017/7/12.
 */

public class QYApplication extends Application {
    private static QYApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        AppClient.initAppClient();
        LoginStatus.getInstance().init(getApplicationContext());

        AlarmHelper.setBaseAlarm(getApplicationContext());
        Log.d("QYApp","set an alarm");

        instance = this;
    }

    public static QYApplication getInstance(){
        return instance;
    }
}

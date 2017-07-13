package com.example.root.mdtest.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.root.mdtest.Common.AlarmHelper;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmHelper.setBaseAlarm(context.getApplicationContext());
        Log.d("BootReceiver","on receive");
    }
}

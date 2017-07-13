package com.example.root.mdtest.Common;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;

import com.example.root.mdtest.Broadcast.PushReceiver;

/**
 * Created by root on 2017/7/13.
 */

public class AlarmHelper {
    public static final int RETURN8 = 511;
    public static final int WEATHER11 = 48;
    public static final int WEATHER17 = 866;

    public static void setAlarm(Context context, Intent intent, int hour, int minute, int second,int flags){
        long nextRTC=getNextRTC(hour,minute,second);
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi=PendingIntent.getBroadcast(context,0,intent,flags);

        am.set(AlarmManager.RTC_WAKEUP,nextRTC,pi);
        Log.d("Alarm","set an alarm "+flags);
    }

    private static long getNextRTC(int hour,int minute,int second){
        Calendar now=Calendar.getInstance();
        Calendar target=(Calendar) now.clone();

        target.set(Calendar.HOUR_OF_DAY,hour);
        target.set(Calendar.MINUTE,minute);
        target.set(Calendar.SECOND,second);
        target.set(Calendar.MILLISECOND,0);
        if(target.before(now)){
            target.add(Calendar.DATE,1);
        }

        return target.getTimeInMillis();
    }

    public static void setBaseAlarm(Context context){
        Intent intent = new Intent(context, PushReceiver.class);
        Bundle bundle=new Bundle();

        bundle.putInt("action",WEATHER11);
        bundle.putInt("hour",11);
        bundle.putInt("minute",0);
        bundle.putInt("second",0);
        bundle.putInt("flag",WEATHER11);

        intent.putExtras(bundle);
        AlarmHelper.setAlarm(context.getApplicationContext(),intent,11,0,0,AlarmHelper.WEATHER11);

        bundle.putInt("action",WEATHER17);
        bundle.putInt("hour",17);
        bundle.putInt("flag",WEATHER17);
        intent.putExtras(bundle);
        AlarmHelper.setAlarm(context.getApplicationContext(),intent,17,0,0,WEATHER17);

        bundle.putInt("action",RETURN8);
        bundle.putInt("hour",8);
        bundle.putInt("flag",RETURN8);
        intent.putExtras(bundle);
        AlarmHelper.setAlarm(context.getApplicationContext(),intent,8,0,0,RETURN8);
    }
}

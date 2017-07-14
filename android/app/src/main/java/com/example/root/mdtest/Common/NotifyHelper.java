package com.example.root.mdtest.Common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.root.mdtest.LoginActivity;
import com.example.root.mdtest.Model.Weather;
import com.example.root.mdtest.R;

/**
 * Created by root on 2017/7/13.
 */

public class NotifyHelper {
    private static final int WEATHERID = 63;
    private static final int RETURNBTN = 376;

    static public void setWeatherNotify(Context context, Weather weather,int numOfUbl) {
        String weatherText = weather.results.get(0).now.text;
        int weatherCode = Integer.parseInt(weather.results.get(0).now.code);

        if (weatherCode < 10 || weatherCode > 20) {
            Log.d("Notify",weatherText);
            return;
        }

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context, LoginActivity.class), 0);
        builder.setContentTitle(weatherText)
                .setContentText("剩余 "+numOfUbl+" 把伞")
                .setTicker("快来借伞啦")
                .setSmallIcon(R.mipmap.logo_new)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), Weather.WEATHERICON[weatherCode]))
                .setDefaults(Notification.FLAG_ONLY_ALERT_ONCE)
                .setContentIntent(pi);

        manager.notify(WEATHERID, builder.build());
    }

    static public void setReturnNotify(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentTitle("记得还伞啊")
                .setTicker("你有伞没还")
                .setDefaults(Notification.FLAG_ONLY_ALERT_ONCE)
                .setSmallIcon(R.mipmap.logo_new);

        manager.notify(RETURNBTN, builder.build());
    }
}

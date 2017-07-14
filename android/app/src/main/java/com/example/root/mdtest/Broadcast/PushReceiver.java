package com.example.root.mdtest.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.root.mdtest.Common.AlarmHelper;
import com.example.root.mdtest.Common.AppClient;
import com.example.root.mdtest.Common.LoginStatus;
import com.example.root.mdtest.Common.NotifyHelper;
import com.example.root.mdtest.Model.LoginResult;
import com.example.root.mdtest.Model.UblMess;
import com.example.root.mdtest.Model.User;
import com.example.root.mdtest.Model.Weather;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PushReceiver extends BroadcastReceiver {
    public static final int WEATHER = 1;
    public static final int RETURNUBL = 2;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("Receiver", "on receiver");

        Bundle bundle = intent.getExtras();
        int action = bundle.getInt("action", 0);
        switch (action) {
            case AlarmHelper.WEATHER11:
            case AlarmHelper.WEATHER17:
                //push base on weather
                AppClient.httpService.getNowWeather(Weather.APPID, Weather.LOCATION, Weather.LANGUAGE, Weather.UNIT).enqueue(new Callback<Weather>() {
                    @Override
                    public void onResponse(Call<Weather> call, Response<Weather> response) {
                        Log.d("weather",response.code()+"");
                        if (response.code() != 200) {
                            return;
                        }

                        final Weather weather = response.body();
                        if (weather == null || weather.status != null || weather.results.size() == 0) {
                            return;
                        }

                        AppClient.httpService.getUblMess(UblMess.AVA).enqueue(new Callback<UblMess>() {
                            @Override
                            public void onResponse(Call<UblMess> call, Response<UblMess> response) {
                                if (response.code() != 200) {
                                    return;
                                }

                                UblMess mess = response.body();
                                if (mess == null || mess.status != 0 || mess.retVal == null) {
                                    return;
                                }
                                NotifyHelper.setWeatherNotify(context, weather, mess.retVal.size());
                            }

                            @Override
                            public void onFailure(Call<UblMess> call, Throwable t) {
                                Log.d("getUblMess", "fail");
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Weather> call, Throwable t) {
                        Log.d("getWeather", "fail");
                    }
                });

                break;
            case AlarmHelper.RETURN8:
                //push base on umbrella borrow_icon
                LoginStatus.getInstance().init(context.getApplicationContext());
                LoginStatus.getInstance().loadStatus();
                if(LoginStatus.getInstance().isLogin()==false){
                    return;
                }
                User user=LoginStatus.getInstance().getUser();
                if(user==null || user.mail==null || user.password==null){
                    return;
                }

                AppClient.httpService.login(user.mail,user.password).enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if(response.code()!=200){
                            return;
                        }

                        LoginResult result=response.body();
                        if(result==null || result.status!=0 || result.user==null){
                            return;
                        }

                        if(result.user.has_borrow==1){
                            NotifyHelper.setReturnNotify(context);
                        }
                        else{
                            Log.d("push return","no borrow");
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                    }
                });

                break;
        }

        //set next alarm
        int hour = bundle.getInt("hour");
        int minute = bundle.getInt("minute");
        int second = bundle.getInt("second");
        int flag = bundle.getInt("flag");
        AlarmHelper.setAlarm(context.getApplicationContext(), intent, hour, minute, second, flag);
    }
}

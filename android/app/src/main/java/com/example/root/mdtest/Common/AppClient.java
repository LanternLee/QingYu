package com.example.root.mdtest.Common;

import android.util.Log;

import com.example.root.mdtest.Model.LoginResult;
import com.example.root.mdtest.Model.UblMess;
import com.example.root.mdtest.Model.UblResult;
import com.example.root.mdtest.Model.User;
import com.example.root.mdtest.Model.Weather;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by root on 2017/7/11.
 */

public class AppClient {
    public interface HttpService{
        @POST("query/borrow")
        Call<List<User>> getBorrowUsers(@Query("borrow") int borrowNum);

        @POST("query/user")
        Call<LoginResult> login(@Query("usermail") String mail,@Query("password") String pass);

        @POST("/update")
        Call<UblResult> handleUbl(@Query("usermail") String mail,@Query("action") int action,@Query("lockid") int lockid);

        @GET("https://api.seniverse.com/v3/weather/now.json")
        Call<Weather> getNowWeather(@Query("key") String appKey,@Query("location") String location,
                                    @Query("language") String language,@Query("unit") String unit);

        @POST("/query/lockid")
        Call<UblMess> getUblMess(@Query("available") String status);
    }

    public static HttpService httpService;

    public static void initAppClient() {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://23.106.130.185:8080/")
                .addConverterFactory(new Converter.Factory() {
                    @Override
                    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
                        Log.d("convertFactory","in");
                        if(!(type instanceof Class<?>)){
                            Log.d("Type",type.toString());
                            return null;
                        }
                        Class<?> c=(Class<?>)type;

                        if(!LoginResult.class.isAssignableFrom(c)){
                            return null;
                        }
                        Log.d("converter","in");
                        return new Converter<ResponseBody, LoginResult>() {
                            @Override
                            public LoginResult convert(ResponseBody value) throws IOException {
                                String json=value.string();
                                JsonParser parser=new JsonParser();
                                JsonElement je=parser.parse(json);
                                JsonObject jo=je.getAsJsonObject();

                                int status=jo.get("status").getAsInt();
                                LoginResult result=new LoginResult();
                                result.status=status;

                                if(status==0){
                                    JsonObject userData=jo.get("retVal").getAsJsonArray().get(0).getAsJsonObject();
                                    User user=new User();

                                    user.mail=userData.get("mail").getAsString();
                                    user.has_borrow=userData.get("has_borrow").getAsInt();
                                    user.score=userData.get("score").getAsInt();
                                    user.ncount=userData.get("ncount").getAsInt();
                                    user.password=userData.get("password").getAsString();
                                    result.user=user;
                                }
                                else{
                                    result.error=jo.get("retVal").getAsString();
                                }

                                return result;
                            }
                        };
                    }
                })
                .addConverterFactory(GsonConverterFactory.create()).build();

        httpService=retrofit.create(HttpService.class);
    }
}

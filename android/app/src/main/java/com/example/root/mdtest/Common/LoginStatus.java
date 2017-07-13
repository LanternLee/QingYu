package com.example.root.mdtest.Common;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.root.mdtest.Model.User;

/**
 * Created by root on 2017/7/12.
 */

public class LoginStatus {
    private static LoginStatus instance;
    private User user;
    private Context context;
    private final String PREFERENCE="LoginStatus";
    private final String MAILKEY="Mail";
    private final String PASSKEY="Password";
    private final String STATUSKEY="Status";
    private final String BORROWKEY="has_borrow";
    private final String NCOUNTKEY="ncount";
    private final String SCOREKEY="score";

    static public LoginStatus getInstance(){
        if(instance==null){
            instance=new LoginStatus();
        }

        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        saveStatus();
    }

    public void refreshUser(User user){

    }

    public boolean isLogin(){
        return user!=null&&!user.mail.isEmpty();
    }

    public void init(Context context){
        this.context=context;
    }

    public void loadStatus(){
        SharedPreferences preferences=context.getSharedPreferences(PREFERENCE,Context.MODE_PRIVATE);
        int status=preferences.getInt(STATUSKEY,-1);
        if(status==-1){
            //no info saved
            user=null;
        }
        else if(status==1){
            //load info about pass & mail
            user=new User();
            user.mail=preferences.getString(MAILKEY,"");
            user.password=preferences.getString(PASSKEY,"");
            user.score=preferences.getInt(SCOREKEY,0);
            user.has_borrow=preferences.getInt(BORROWKEY,0);
            user.ncount=preferences.getInt(NCOUNTKEY,0);
        }
    }

    public void saveStatus(){
        SharedPreferences preferences=context.getSharedPreferences(PREFERENCE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();

        if(!isLogin()){
            editor.putInt(STATUSKEY,-1);
        }
        else {
            editor.putInt(STATUSKEY,1);
            editor.putString(MAILKEY, user.mail);
            editor.putString(PASSKEY, user.password);
            editor.putInt(NCOUNTKEY,user.ncount);
            editor.putInt(SCOREKEY,user.score);
            editor.putInt(BORROWKEY,user.has_borrow);
        }

        editor.commit();
    }
}

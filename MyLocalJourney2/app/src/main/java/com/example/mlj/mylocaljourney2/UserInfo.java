package com.example.mlj.mylocaljourney2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by alvin on 2016/6/23.
 */
public class UserInfo {

    private Activity activity;

    // constructor
    public UserInfo(Activity activity) {
        this.activity = activity;
    }

    //private String mUserId;
    //private String mFacebookId;
    //private int mIsUserIdValid;
    public void setFacebookId(String facebookId){
        // use SharedPreferences
        SharedPreferences userInfoDatabase = activity.getSharedPreferences("userInfoDatabase", Context.MODE_PRIVATE);
        userInfoDatabase.edit().putString("FACEBOOKID",facebookId).commit();
    };
    public String getFacebookId(){
        String stFacebookId;
        SharedPreferences userInfoDatabase = activity.getSharedPreferences("userInfoDatabase", Context.MODE_PRIVATE);
        stFacebookId=userInfoDatabase.getString("FACEBOOKID", "null");
        return stFacebookId;
    };
    public void setUserId(String userId){
        // use SharedPreferences
        SharedPreferences userInfoDatabase = activity.getSharedPreferences("userInfoDatabase", Context.MODE_PRIVATE);
        userInfoDatabase.edit().putString("USERID",userId).commit();
    };
    public String getUserId(){
        String UserId;
        SharedPreferences userInfoDatabase = activity.getSharedPreferences("userInfoDatabase", Context.MODE_PRIVATE);
        UserId=userInfoDatabase.getString("USERID", "null");
        return UserId;
    };
    public void setIsLoginStatus(int loginStatus){
        // use SharedPreferences
        SharedPreferences userInfoDatabase = activity.getSharedPreferences("userInfoDatabase", Context.MODE_PRIVATE);
        userInfoDatabase.edit().putInt("LOGIN_STATUS", loginStatus).commit();
    };
    public int getIsLoginStatus(){
        int isLoginStatus = 0;
        SharedPreferences userInfoDatabase = activity.getSharedPreferences("userInfoDatabase", Context.MODE_PRIVATE);
        isLoginStatus = userInfoDatabase.getInt("LOGIN_STATUS", -1);
        return isLoginStatus;
    };

    public void setLoginType(String loginType){
        // use SharedPreferences
        SharedPreferences userInfoDatabase = activity.getSharedPreferences("userInfoDatabase", Context.MODE_PRIVATE);
        userInfoDatabase.edit().putString("LOGIN_TYPE",loginType).commit();
    };
    public String getLoginType(){
        String LoginType;
        SharedPreferences userInfoDatabase = activity.getSharedPreferences("userInfoDatabase", Context.MODE_PRIVATE);
        LoginType=userInfoDatabase.getString("LOGIN_TYPE", "null");
        return LoginType;
    };
}

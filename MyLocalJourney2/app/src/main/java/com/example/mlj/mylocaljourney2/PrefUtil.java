package com.example.mlj.mylocaljourney2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by alvin on 2016/5/15.
 */
public class PrefUtil {

    private Activity activity;

    // constructor
    public PrefUtil(Activity activity) {
        this.activity = activity;
    }

    public void saveAccessToken(String token) {
        Utils.l("Libo debug : token "+token);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", token);
        editor.apply();

    }

    public String getToken() {
        Utils.l("Libo debug ");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        return sp.getString("token", null);
    }

    public void clearToken() {
        Utils.l("Libo debug ");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }
}

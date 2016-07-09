package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.Context;
/**
 * Created by Alex on 29.06.2016.
 */
public class DevintensiveApplication extends Application {

    public static SharedPreferences sSharedPreferences;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        //контекст для ресуров
        mContext = this;

        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public static Context getContext() {
        return mContext;
    }

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

}

package com.softdesign.devintensive.Data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevintensiveApplication;

import com.softdesign.devintensive.R;


import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 29.06.2016.
 */
public class PreferencesManager {

    private SharedPreferences mSharedPreferences;

    private static final String[] USER_FIELDS = {ConstantManager.USER_PHONE_KEY,
            ConstantManager.USER_MAIL_KEY,
            ConstantManager.USER_VK_KEY,
            ConstantManager.USER_GIT_KEY,
            ConstantManager.USER_BIO_KEY
    };
    public PreferencesManager() {
        this.mSharedPreferences = DevintensiveApplication.getSharedPreferences();
    }

    public void saveUserProfileData(List<String> userFields) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (int i=0; i< USER_FIELDS.length; i++) {
            editor.putString(USER_FIELDS[i], userFields.get(i));
        }
        editor.apply();
    }

    public List<String> loadUserProfileData() {
        List<String> userFields = new ArrayList<>();

        userFields.add(mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY,
                DevintensiveApplication.getContext().getResources().getString(R.string.user_phone_number)));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_MAIL_KEY,
                DevintensiveApplication.getContext().getResources().getString(R.string.user_email)));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_VK_KEY,
                DevintensiveApplication.getContext().getResources().getString(R.string.user_vk)));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_GIT_KEY,
                DevintensiveApplication.getContext().getResources().getString(R.string.user_git)));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_BIO_KEY,
                DevintensiveApplication.getContext().getResources().getString(R.string.user_bio)));

        return userFields;
    }

    public void saveUserPhoto(Uri uri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY, uri.toString());

        editor.apply();
    }

    public Uri loadUserPhoto() {
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY,
                "android.resource://com.softdesign.devintensive/drawable/userphoto"));
    }
}


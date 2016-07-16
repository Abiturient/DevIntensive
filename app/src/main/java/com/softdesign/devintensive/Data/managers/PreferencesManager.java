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

    private static final String[] USER_VALUES = {
            ConstantManager.USER_RATING_VALUE,
            ConstantManager.USER_CODE_LINES_VALUE,
            ConstantManager.USER_PROJECT_COUNT_VALUE
    };

    private static final String[] USER_NAME = {
            ConstantManager.USER_FIRST_NAME_KEY,
            ConstantManager.USER_SECOND_NAME_KEY
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

    public List<String> loadUserProfileValues() {
        List<String> userValues = new ArrayList<>();
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_RATING_VALUE,"0"));
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_CODE_LINES_VALUE,"0"));
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_PROJECT_COUNT_VALUE,"0"));

        return userValues;
    }

    public void saveUserProfileValues(int[] userValues) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (int i = 0; i < USER_VALUES.length; i++) {
            editor.putString(USER_VALUES[i], String.valueOf(userValues[i]));
        }
        editor.apply();
    }

    public void saveUserName(List<String> name) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (int i = 0; i < USER_NAME.length; i++) {
            editor.putString(USER_NAME[i], name.get(i));
        }
        editor.apply();
    }

    public String loadUserName() {
        String userName = mSharedPreferences.getString(ConstantManager.USER_FIRST_NAME_KEY, "Имя")
                + " " +
        mSharedPreferences.getString(ConstantManager.USER_SECOND_NAME_KEY, "Фамилия");

        return userName;
    }

    public Uri loadUserPhoto() {
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY,
                "android.resource://com.softdesign.devintensive/drawable/userphoto"));
    }

    public void saveAuthToken(String authToken) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.AUTH_TOKEN_KEY, authToken);
        editor.apply();
    }

    public String getAuthToken() {
        return mSharedPreferences.getString(ConstantManager.AUTH_TOKEN_KEY, "null");
    }

    public void saveUserId(String userId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_ID_KEY, userId);
        editor.apply();
    }

    public String getUserId() {
        return mSharedPreferences.getString(ConstantManager.USER_ID_KEY, "null");
    }

    public void saveUserAvatar(Uri uri) {
        if (uri != null) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();

            editor.putString(ConstantManager.USER_AVATAR_KEY, uri.toString());
            editor.apply();
        }
    }
}


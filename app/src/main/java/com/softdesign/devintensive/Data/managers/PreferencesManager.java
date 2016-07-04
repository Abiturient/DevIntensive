package com.softdesign.devintensive.Data.managers;

import android.content.SharedPreferences;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevintensiveApplication;

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

        userFields.add(mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY, "+7(921)4401247"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_MAIL_KEY, "p.oblivion@yandex.ru"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_VK_KEY, "http://vk.com/id367577291"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_GIT_KEY, "https://github.com/Abiturient/DevIntensive"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_BIO_KEY, "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."));

        return userFields;
    }
}

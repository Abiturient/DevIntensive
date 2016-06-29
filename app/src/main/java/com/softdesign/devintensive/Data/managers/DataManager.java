package com.softdesign.devintensive.Data.managers;

/**
 * Created by Alex on 29.06.2016.
 */
public class DataManager {

    private static DataManager INSTANCE = null;
    private PreferencesManager mPreferencesManager;

    public DataManager() {
        this.mPreferencesManager = new PreferencesManager();
    }

    public static DataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }
}

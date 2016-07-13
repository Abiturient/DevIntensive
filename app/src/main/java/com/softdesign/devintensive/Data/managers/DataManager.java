package com.softdesign.devintensive.Data.managers;

import android.content.Context;

import com.softdesign.devintensive.Data.network.RestService;
import com.softdesign.devintensive.Data.network.ServiceGenerator;
import com.softdesign.devintensive.Data.network.req.UserLoginReq;
import com.softdesign.devintensive.Data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.DevintensiveApplication;

import retrofit2.Call;

/**
 * Created by Alex on 29.06.2016.
 */
public class DataManager {

    private static DataManager INSTANCE = null;
    private PreferencesManager mPreferencesManager;
    private Context mContext;

    private RestService mRestService;

    public DataManager() {

        this.mPreferencesManager = new PreferencesManager();
        this.mContext = DevintensiveApplication.getContext();
        this.mRestService = ServiceGenerator.createService(RestService.class);

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

    public Context getContext() {
        return mContext;
    }

    //region ============ Network ================
    public Call<UserModelRes> loginUser(UserLoginReq userLoginReq){
        return mRestService.loginUser(userLoginReq);
    }
    //end region

    // region =============Database===============

//    public Call<UserModelRes> loginUser(UserLoginReq userLoginReq){
//        return mRestService.loginUser(userLoginReq);
//    }
    //endregion

}

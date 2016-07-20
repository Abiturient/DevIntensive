package com.softdesign.devintensive.Data.managers;

import android.content.Context;

import com.softdesign.devintensive.Data.network.PicassoCache;
import com.softdesign.devintensive.Data.network.RestService;
import com.softdesign.devintensive.Data.network.ServiceGenerator;
import com.softdesign.devintensive.Data.network.req.UserLoginReq;
import com.softdesign.devintensive.Data.network.res.UserListRes;
import com.softdesign.devintensive.Data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.DevintensiveApplication;
import com.squareup.picasso.Picasso;

import retrofit2.Call;

/**
 * Created by Alex on 29.06.2016.
 */
public class DataManager {

    private static DataManager INSTANCE = null;
    private PreferencesManager mPreferencesManager;
    private Context mContext;

    private Picasso mPicasso;
    //private DaoSession mDaoSession;

    private RestService mRestService;

    public DataManager() {

        this.mPreferencesManager = new PreferencesManager();
        this.mContext = DevintensiveApplication.getContext();
        this.mRestService = ServiceGenerator.createService(RestService.class);

        this.mPicasso = new PicassoCache(mContext).getPicassoInstance();


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

    public Picasso getPicasso() {
        return mPicasso;
    }

    //region ============ Network ================
    public Call<UserModelRes> loginUser(UserLoginReq userLoginReq){
        return mRestService.loginUser(userLoginReq);
    }

    public Call<UserModelRes> authByToken(){
        return mRestService.authUserByToken(mPreferencesManager.getUserId());
    }

    public Call<UserListRes> getUserList() {
        return mRestService.getUserList();
    }
    //end region

    // region =============Database===============

//    public Call<UserModelRes> loginUser(UserLoginReq userLoginReq){
//        return mRestService.loginUser(userLoginReq);
//    }
    //endregion

}

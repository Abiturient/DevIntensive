package com.softdesign.devintensive.Data.managers;

import android.content.Context;

import com.softdesign.devintensive.Data.network.PicassoCache;
import com.softdesign.devintensive.Data.network.RestService;
import com.softdesign.devintensive.Data.network.ServiceGenerator;
import com.softdesign.devintensive.Data.network.req.UserLoginReq;
import com.softdesign.devintensive.Data.network.res.UserListRes;
import com.softdesign.devintensive.Data.network.res.UserModelRes;
import com.softdesign.devintensive.Data.storage.models.DaoSession;
import com.softdesign.devintensive.Data.storage.models.User;
import com.softdesign.devintensive.Data.storage.models.UserDao;
import com.softdesign.devintensive.utils.DevintensiveApplication;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by Alex on 29.06.2016.
 */
public class DataManager {

    private static DataManager INSTANCE = null;
    private PreferencesManager mPreferencesManager;
    private Context mContext;

    private Picasso mPicasso;
    private DaoSession mDaoSession;

    private RestService mRestService;

    public DataManager() {

        this.mPreferencesManager = new PreferencesManager();
        this.mContext = DevintensiveApplication.getContext();
        this.mRestService = ServiceGenerator.createService(RestService.class);

        this.mPicasso = new PicassoCache(mContext).getPicassoInstance();

        this.mDaoSession    = DevintensiveApplication.getDaoSession();


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

    public Call<UserListRes> getUserListFromNetwork() {
        return mRestService.getUserList();
    }
    //end region

    // region =============Database===============

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public List<User> getUserListFromDb() {
        List<User> userList = new ArrayList<>();

        try {
            userList = mDaoSession.queryBuilder(User.class)
                    .where(UserDao.Properties.CodeLines.gt(0))
                    .orderDesc(UserDao.Properties.CodeLines)
                    .build()
            .list();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userList;
    }

    public List<User> getUserListByName(String query) {
        List<User> userList = new ArrayList<>();
        try {
            userList = mDaoSession.queryBuilder(User.class)
                    .where(UserDao.Properties.Rating.gt(0),
                            UserDao.Properties.SearchName.like("%"+query.toUpperCase()+"%"))
                    .orderDesc(UserDao.Properties.CodeLines)
                    .build()
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userList;
    }

    //endregion

}

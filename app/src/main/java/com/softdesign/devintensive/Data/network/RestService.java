package com.softdesign.devintensive.Data.network;

import com.softdesign.devintensive.Data.network.req.UserLoginReq;
import com.softdesign.devintensive.Data.network.res.UserModelRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Alex on 13.07.2016.
 */
public interface RestService {

    /*@Headers({
            "Custom-header : my header Value"
    })*/

    @POST("login")
    Call<UserModelRes> loginUser (@Body UserLoginReq req);
}

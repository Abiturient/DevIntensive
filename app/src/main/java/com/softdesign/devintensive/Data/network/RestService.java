package com.softdesign.devintensive.Data.network;

import com.softdesign.devintensive.Data.network.req.UserLoginReq;
import com.softdesign.devintensive.Data.network.res.UserListRes;
import com.softdesign.devintensive.Data.network.res.UserModelRes;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Alex on 13.07.2016.
 */
public interface RestService {

    /*@Headers({
            "Custom-header : my header Value"
    })*/

    @POST("login")
    Call<UserModelRes> loginUser (@Body UserLoginReq req);

    @Multipart
    @POST("user/{userId}/publicValues/profilePhoto")
    Call<ResponseBody> uploadUserPhoto(@Path("userId") String userId,
                                       @Part MultipartBody.Part photo);

    @GET("user/{userId}")
    Call<UserModelRes> authUserByToken(@Path("userId") String userId);


    @GET("user/list?orderBy=rating")
    Call<UserListRes> getUserList();

}

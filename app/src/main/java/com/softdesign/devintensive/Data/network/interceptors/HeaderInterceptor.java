package com.softdesign.devintensive.Data.network.interceptors;

import com.softdesign.devintensive.Data.managers.DataManager;
import com.softdesign.devintensive.Data.managers.PreferencesManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Alex on 13.07.2016.
 */
public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        PreferencesManager preferencesManager = DataManager.getInstance().getPreferencesManager();

        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .header("X-Access-Token", preferencesManager.getAuthToken())
                .header("Request-User-Id", preferencesManager.getUserId())
                .header("User-Agent", "DevIntensive")
                .header("Cache-Control", "max-age=" +(60*60*24) );

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }


}

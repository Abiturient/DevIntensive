package com.softdesign.devintensive.Data.network;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

/**
 * Created by Alex on 20.07.2016.
 */
public class PicassoCache {

    private Context mContext;
    private Picasso mPicassoInstance;

    public PicassoCache(Context context) {
        mContext = context;
        OkHttp3Downloader okHttp3Downloader = new OkHttp3Downloader(context, Integer.MAX_VALUE);
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(okHttp3Downloader);

        mPicassoInstance = builder.build();
        Picasso.setSingletonInstance(mPicassoInstance);
    }

    public Picasso getPicassoInstance() {
        if (mPicassoInstance == null) {
            new PicassoCache(mContext);
        }
        return mPicassoInstance;
    }

}

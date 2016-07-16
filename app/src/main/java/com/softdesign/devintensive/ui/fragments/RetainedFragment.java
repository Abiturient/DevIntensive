package com.softdesign.devintensive.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;

import com.softdesign.devintensive.Data.network.res.UserListRes;

import java.util.List;

/**
 * Created by Alex on 16.07.2016.
 */
public class RetainedFragment extends Fragment {

    private List<UserListRes.UserData> mData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    public List<UserListRes.UserData> getData() {
        return mData;
    }

    public void setData(List<UserListRes.UserData> data) {
        mData = data;
    }
}
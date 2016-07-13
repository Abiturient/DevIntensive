package com.softdesign.devintensive.Data.network.req;

/**
 * Created by Alex on 13.07.2016.
 */
public class UserLoginReq {

    private String email;
    private String password;

    public UserLoginReq(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

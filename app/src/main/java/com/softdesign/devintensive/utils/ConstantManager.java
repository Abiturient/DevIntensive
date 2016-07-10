package com.softdesign.devintensive.utils;

/**
 * Created by Alex on 25.06.2016.
 */
public interface ConstantManager {

    //преяикс для Log
    String TAG_PREFIX = "DEV ";

    //ключ редактирования
    String EDIT_MODE_KEY = "EDIT_MODE_KEY";

    //ключ инфо о юзере
    String USER_PHONE_KEY = "USER_PHONE_KEY";
    String USER_MAIL_KEY = "USER_MAIL_KEY";
    String USER_VK_KEY ="USER_VK_KEY";
    String USER_GIT_KEY = "USER_GIT_KEY";
    String USER_BIO_KEY = "USER_BIO_KEY";
    String USER_PHOTO_KEY = "USER_PHOTO_KEY";

    int LOAD_PROFILE_PHOTO = 1;
    int REQUEST_CAMERA_PICTURE = 99;
    int REQUEST_GALLERY_PICTURE = 88;

    int PERMISSION_REQUEST_SETTINGS_CODE = 101;
    int CAMERA_REQUEST_PERMISSION_CODE = 102;
    int CALL_PHONE_REQUEST_PERMISSIION_CODE = 200;

    String REQUEST_LOGIN_KEY = "REQUEST_LOGIN_KEY";

    int ERROR_TIMER_LENGTH_NORMAL   = 3200;
    int ERROR_TIMER_LENGTH_LONG     = 4800;
    int ERROR_TIMER_LENGTH_SHORT    = 1600;
}

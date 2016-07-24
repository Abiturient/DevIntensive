package com.softdesign.devintensive.utils;

/**
 * Created by Alex on 13.07.2016.
 */
public interface AppConfig {

    String BASE_URL = "http://devintensive.softdesign-apps.ru/api/";

    long MAX_CONNECT_TIMEOUT = 60000;
    long MAX_READ_TIMEOUT = 60000;
    int START_DELAY = 1500;
}

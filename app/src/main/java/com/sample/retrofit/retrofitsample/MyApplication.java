package com.sample.retrofit.retrofitsample;

import android.app.Application;

import com.google.gson.Gson;
import com.sample.retrofit.retrofitsample.common.ApiManager;

public class MyApplication extends Application {

    /**
     * Gson 객체
     */
    private Gson gson;

    /**
     * ApiManager 객체
     */
    private ApiManager apiManager;


    private static class Singleton {
        private static final MyApplication instance = new MyApplication();
    }

    public static MyApplication getInstance() {
        return Singleton.instance;
    }


    /*
     * Gson 리턴하는 메소드
     */
    public Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    /**
     * ApiManager 리턴하는 메소드
     */
    public ApiManager getApiManager() {
        if (apiManager == null) {
            apiManager = new ApiManager();
        }
        return apiManager;
    }

}

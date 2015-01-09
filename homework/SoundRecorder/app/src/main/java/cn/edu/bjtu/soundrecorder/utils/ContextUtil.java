package cn.edu.bjtu.soundrecorder.utils;

import android.app.Application;

/**
 * Created by Jake on 2015/1/9.
 */
public class ContextUtil extends Application {
    private static ContextUtil instance;

    public static ContextUtil getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
    }
}
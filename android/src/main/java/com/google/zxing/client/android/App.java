package com.google.zxing.client.android;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

/**
 * @author yfc
 * @version V1.0
 * @since 2022/08/10 11:24
 */
public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}

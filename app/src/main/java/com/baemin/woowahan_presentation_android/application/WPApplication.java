package com.baemin.woowahan_presentation_android.application;

import android.app.Application;

import com.baemin.woowahan_presentation_android.util.PreferencesManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by leetaejun on 2016. 2. 14..
 */
public class WPApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        PreferencesManager.initializeInstance(this);
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
    }
}

package com.baemin.woowahan_presentation_android.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.baemin.woowahan_presentation_android.model.CategoryModel;
import com.baemin.woowahan_presentation_android.model.TeamModel;
import com.baemin.woowahan_presentation_android.model.UserModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by leetaejun on 2016. 2. 14..
 */
public class PreferencesManager {

    // SHAREDPREFERENCES NAME
    private static final String PREF_NAME = "com.baemin.woowahan_presentation_android.SHAREDPREFERENCES";

    // PROPERTY
    private static final String KEY_CATEGORY = "com.baemin.woowahan_presentation_android.KEY_CATEGORY";
    private static final String KEY_TEAM = "com.baemin.woowahan_presentation_android.KEY_TEAM";
    private static final String KEY_ACCESSTOKEN = "com.baemin.woowahan_presentation_android.KEY_ACCESSTOKEN";
    private static final String KEY_USER = "com.baemin.woowahan_presentation_android.KEY_USER";

    // VAR
    private static PreferencesManager sInstance;
    private final SharedPreferences mPref;

    private PreferencesManager(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
    }

    public static synchronized PreferencesManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(PreferencesManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }

    public void setCategories(List<CategoryModel> categories) {
        Gson gson = new Gson();
        String categoriesString = gson.toJson(categories);
        mPref.edit()
                .putString(KEY_CATEGORY, categoriesString)
                .commit();
    }

    public List<CategoryModel> getCategories() {
        Gson gson = new Gson();
        String categoriesString = mPref.getString(KEY_CATEGORY, null);
        Type type = new TypeToken<List<CategoryModel>>() {}.getType();

        if (categoriesString != null) {
            return gson.fromJson(categoriesString, type);
        } else {
            return null;
        }
    }

    public void setTeams(List<TeamModel> teams) {
        Gson gson = new Gson();
        String teamsString = gson.toJson(teams);
        mPref.edit()
                .putString(KEY_TEAM, teamsString)
                .commit();
    }

    public List<TeamModel> getTeams() {
        Gson gson = new Gson();
        String teamsString = mPref.getString(KEY_TEAM, null);
        Type type = new TypeToken<List<TeamModel>>() {}.getType();

        if (teamsString != null) {
            return gson.fromJson(teamsString, type);
        } else {
            return null;
        }
    }

    public void setAccessToken(String accessToken) {
        mPref.edit()
                .putString(KEY_ACCESSTOKEN, accessToken)
                .commit();
    }

    public String getAccessToken() {
        return mPref.getString(KEY_ACCESSTOKEN, null);
    }

    public void setUser(UserModel user) {
        Gson gson = new Gson();
        String userString = gson.toJson(user);
        mPref.edit()
                .putString(KEY_USER, userString)
                .commit();
    }

    public UserModel getUser() {
        Gson gson = new Gson();
        String userString = mPref.getString(KEY_USER, null);
        if (userString != null) {
            return gson.fromJson(userString, UserModel.class);
        } else {
            return null;
        }
    }

    public void remove(String key) {
        mPref.edit()
                .remove(key)
                .commit();
    }

    public boolean clear() {
        return mPref.edit()
                .clear()
                .commit();
    }
}

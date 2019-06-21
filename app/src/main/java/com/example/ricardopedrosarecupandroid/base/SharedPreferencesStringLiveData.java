package com.example.ricardopedrosarecupandroid.base;

import android.content.SharedPreferences;

public class SharedPreferencesStringLiveData extends SharedPreferencesLiveData<String> {

    public SharedPreferencesStringLiveData(SharedPreferences sharedPreferences,
                                           String key,
                                           String defaultValue) {
        super(sharedPreferences, key, defaultValue);
    }

    @Override
    protected String getValueFromPreferences(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

}
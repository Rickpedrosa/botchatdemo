package com.example.ricardopedrosarecupandroid.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ricardopedrosarecupandroid.data.local.AppDatabase;

public class MainActivityViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final AppDatabase database;

    public MainActivityViewModelFactory(Application application, AppDatabase appDatabase) {
        this.application = application;
        this.database = appDatabase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainActivityViewModel(application, database);
    }
}

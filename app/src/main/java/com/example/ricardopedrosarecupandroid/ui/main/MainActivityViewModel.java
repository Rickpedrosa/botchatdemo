package com.example.ricardopedrosarecupandroid.ui.main;

import android.app.Application;
import android.app.ListActivity;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.preference.PreferenceManager;

import com.example.ricardopedrosarecupandroid.R;
import com.example.ricardopedrosarecupandroid.base.SharedPreferencesLiveData;
import com.example.ricardopedrosarecupandroid.base.SharedPreferencesStringLiveData;
import com.example.ricardopedrosarecupandroid.data.Repository;
import com.example.ricardopedrosarecupandroid.data.RepositoryImpl;
import com.example.ricardopedrosarecupandroid.data.local.AppDatabase;
import com.example.ricardopedrosarecupandroid.data.local.entity.LiveChat;
import com.example.ricardopedrosarecupandroid.data.local.entity.MessageChat;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    // TODO
    private final Repository repo;
    private final LiveData<String> saveMessagePreference;
    private final LiveData<String> favIconPositionPreference;
    private MutableLiveData<Boolean> fabTrigger = new MutableLiveData<>();
    private String userMessage;
    private String positionReferenceValue;
    private String saveMessageValue;

    public MainActivityViewModel(@NonNull Application application, AppDatabase database) {
        super(application);
        this.repo = new RepositoryImpl(database.messagesDao(), database.liveChatDao());
        saveMessagePreference = new SharedPreferencesStringLiveData(
                PreferenceManager.getDefaultSharedPreferences(application),
                application.getResources().getString(R.string.save_answer_key),
                application.getResources().getString(R.string.save_answer_defaultValue)
        );
        favIconPositionPreference = new SharedPreferencesStringLiveData(
                PreferenceManager.getDefaultSharedPreferences(application),
                application.getResources().getString(R.string.star_pos_key),
                application.getResources().getString(R.string.star_pos_defaultValue)
        );
    }

    //APP

    public LiveData<String> getSaveMessagePreference() {
        return saveMessagePreference;
    }

    public LiveData<String> getFavIconPositionPreference() {
        return favIconPositionPreference;
    }

    public String getPositionReferenceValue() {
        return positionReferenceValue;
    }

    public void setPositionReferenceValue(String positionReferenceValue) {
        this.positionReferenceValue = positionReferenceValue;
    }

    public String getSaveMessageValue() {
        return saveMessageValue;
    }

    public void setSaveMessageValue(String saveMessageValue) {
        this.saveMessageValue = saveMessageValue;
    }

    //*************************************************************************************

    //MAIN FRAGMENT
    public void addMessageToLiveChat(LiveChat chat) {
        repo.addMessageToChat(chat);
    }

    public LiveData<List<LiveChat>> getLiveChat() {
        return repo.getLiveChat();
    }

    public void updateMessageLiveChat(LiveChat chat) {
        repo.updateMessage(chat);
    }

    public void deleteMessageLiveChat(LiveChat liveChat) {
        repo.deleteMessage(liveChat);
    }

    public void clearLiveChat() {
        repo.deleteAll();
    }

    public void triggerMessage() {
        fabTrigger.setValue(true);
    }

    public void triggerUpdate() {
        fabTrigger.setValue(false);
    }

    public void triggerDelete() {
        fabTrigger.setValue(false);
    }

    public LiveData<MessageChat> theFuckingBot() {
        return Transformations.switchMap(fabTrigger, input -> {
            if (input) {
                return repo.getRandomBotMessageOmegaLUL();
            } else {
                return null;
            }
        });
    }

    public LiveData<Integer> scrollPositionPointer() {
        return Transformations.switchMap(
                fabTrigger, input -> {
                    if (input) {
                        return repo.getLiveChatSize();
                    } else {
                        return null;
                    }
                }
        );
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public void updateFavoriteState(long id) {
        repo.setMessageToBeFavorite(id);
    }

    //*******************************************+
}

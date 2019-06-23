package com.example.ricardopedrosarecupandroid.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.example.ricardopedrosarecupandroid.R;
import com.example.ricardopedrosarecupandroid.base.SharedPreferencesStringLiveData;
import com.example.ricardopedrosarecupandroid.data.Repository;
import com.example.ricardopedrosarecupandroid.data.RepositoryImpl;
import com.example.ricardopedrosarecupandroid.data.local.AppDatabase;
import com.example.ricardopedrosarecupandroid.data.local.entity.LiveChat;
import com.example.ricardopedrosarecupandroid.data.local.entity.MessageChat;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    // TODO
    private final Repository repo;
    private final LiveData<String> saveMessagePreference;
    private final LiveData<String> favIconPositionPreference;
    private MutableLiveData<Boolean> recyclerPosition = new MutableLiveData<>();

    MainActivityViewModel(@NonNull Application application, AppDatabase database) {
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

    //*************************************************************************************

    //MAIN FRAGMENT
    public void addMessageToLiveChat(LiveChat chat) {
        repo.addMessageToChat(chat);
    }

    public LiveData<List<LiveChat>> getLiveChat() {
        return repo.getLiveChat();
    }

    public LiveData<List<MessageChat>> getBotMessages() {
        return repo.getBotMessages();
    }

    public void deleteMessageLiveChat(LiveChat liveChat) {
        repo.deleteMessage(liveChat);
    }

    public void clearLiveChat() {
        repo.deleteAll();
    }

    public void updateFavoriteState(long id) {
        repo.setMessageToBeFavorite(id);
    }

    public MutableLiveData<Boolean> getRecyclerPosition() {
        return recyclerPosition;
    }

    public void updateRecyclerPosition() {
        recyclerPosition.setValue(true);
    }

    //*******************************************+

    public LiveData<List<String>> getBotValues() {
        return repo.getAllBotValues();
    }

    public void addBotMessage(MessageChat messageChat){
        repo.insertMessage(messageChat);
    }
}

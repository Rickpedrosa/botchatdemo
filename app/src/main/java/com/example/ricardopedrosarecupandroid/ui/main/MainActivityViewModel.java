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
    private final Application application;
    private MutableLiveData<Boolean> fabTrigger = new MutableLiveData<>();
    private String userMessage;

    public MainActivityViewModel(@NonNull Application application, AppDatabase database) {
        super(application);
        this.application = application;
        this.repo = new RepositoryImpl(database.messagesDao(), database.liveChatDao());
    }

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

    public void triggerDelete() {
        fabTrigger.setValue(false);
    }

    public LiveData<MessageChat> theFuckingBot() {
        return Transformations.switchMap(fabTrigger, new Function<Boolean, LiveData<MessageChat>>() {
            @Override
            public LiveData<MessageChat> apply(Boolean input) {
                if (input) {
                    return repo.getRandomBotMessageOmegaLUL();
                } else {
                    return null;
                }
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

    //*******************************************+
}

package com.example.ricardopedrosarecupandroid.data;

import androidx.lifecycle.LiveData;

import com.example.ricardopedrosarecupandroid.data.local.entity.LiveChat;
import com.example.ricardopedrosarecupandroid.data.local.entity.MessageChat;

import java.util.List;

public interface Repository {

    LiveData<List<MessageChat>> getBotMessages();

    LiveData<MessageChat> getGreetingMessage();

   // LiveData<List<MessageChat>> getRandomBotMessages();

    void insertMessage(MessageChat messageChat);

    void insertPredefinedBotMessages(MessageChat[] messageChats);

    void deleteMessage(MessageChat messageChat);

    void updateMessage(MessageChat messageChat);

    LiveData<List<LiveChat>> getLiveChat();

    void addMessageToChat(LiveChat chat);

    void updateMessage(LiveChat chat);

    void deleteMessage(LiveChat chat);

    void deleteAll();

    LiveData<Integer> getLiveChatSize();
    LiveData<MessageChat> getRandomBotMessageOmegaLUL();

    void setMessageToBeFavorite(long id);
    LiveData<List<String>> getAllBotValues();


}

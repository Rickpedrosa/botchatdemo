package com.example.ricardopedrosarecupandroid.data;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.ricardopedrosarecupandroid.data.local.daos.LiveChatDao;
import com.example.ricardopedrosarecupandroid.data.local.daos.MessagesDao;
import com.example.ricardopedrosarecupandroid.data.local.entity.LiveChat;
import com.example.ricardopedrosarecupandroid.data.local.entity.MessageChat;

import java.util.List;

public class RepositoryImpl implements Repository {

    private final MessagesDao messagesDao;
    private final LiveChatDao liveChatDao;

    public RepositoryImpl(MessagesDao messagesDao, LiveChatDao liveChatDao) {
        this.messagesDao = messagesDao;
        this.liveChatDao = liveChatDao;
    }

    @Override
    public LiveData<List<MessageChat>> getBotMessages() {
        return messagesDao.getBotMessages();
    }

    @Override
    public LiveData<MessageChat> getGreetingMessage() {
        return messagesDao.getGreetingMessage();
    }

    @Override
    public void insertMessage(MessageChat messageChat) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> messagesDao.insertMessage(messageChat));
    }

    @Override
    public void insertPredefinedBotMessages(MessageChat[] messageChats) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> messagesDao.insertPredefinedBotMessages(messageChats));
    }

    @Override
    public void deleteMessage(MessageChat messageChat) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> messagesDao.deleteMessage(messageChat));
    }

    @Override
    public void updateMessage(MessageChat messageChat) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> messagesDao.updateMessage(messageChat));
    }

    @Override
    public LiveData<List<LiveChat>> getLiveChat() {
        return liveChatDao.getLiveChat();
    }

    @Override
    public void addMessageToChat(LiveChat chat) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> liveChatDao.addMessageToChat(chat));
    }

    @Override
    public void updateMessage(LiveChat chat) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> liveChatDao.updateMessage(chat));
    }

    @Override
    public void deleteMessage(LiveChat chat) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> liveChatDao.deleteMessage(chat));
    }

    @Override
    public void deleteAll() {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(liveChatDao::deleteAll);
    }

    @Override
    public LiveData<Integer> getLiveChatSize() {
        return liveChatDao.getLiveChatSize();
    }

    @Override
    public LiveData<MessageChat> getRandomBotMessageOmegaLUL() {
        return messagesDao.getRandomBotMessageOmegaLUL();
    }

    @Override
    public void setMessageToBeFavorite(long id) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> liveChatDao.setMessageToBeFavorite(id));
    }
}

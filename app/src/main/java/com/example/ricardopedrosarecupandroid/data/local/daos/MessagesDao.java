package com.example.ricardopedrosarecupandroid.data.local.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ricardopedrosarecupandroid.data.local.entity.MessageChat;

import java.util.List;

@Dao
public interface MessagesDao {

    @Query("SELECT id, value, botOrUser, isFav, date_hour " +
            "FROM message WHERE botOrUser = 0")
    LiveData<List<MessageChat>> getBotMessages();

    @Query("SELECT id, value, botOrUser, isFav, date_hour FROM message " +
            "WHERE id = 1")
    LiveData<MessageChat> getGreetingMessage();

    @Query("SELECT * FROM message ORDER BY RANDOM()")
    LiveData<MessageChat> getRandomBotMessageOmegaLUL();

    @Query("SELECT value FROM message ORDER BY id")
    LiveData<List<String>> getAllBotValues();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessage(MessageChat messageChat);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPredefinedBotMessages(MessageChat[] messageChats);

    @Delete
    void deleteMessage(MessageChat messageChat);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMessage(MessageChat messageChat);
}

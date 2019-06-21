package com.example.ricardopedrosarecupandroid.data.local.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ricardopedrosarecupandroid.data.local.entity.LiveChat;

import java.util.List;

@Dao
public interface LiveChatDao {

    @Query("SELECT * FROM livechat ORDER BY id")
    LiveData<List<LiveChat>> getLiveChat();

    @Query("SELECT COUNT(id) FROM livechat")
    LiveData<Integer> getLiveChatSize();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMessageToChat(LiveChat chat);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMessage(LiveChat chat);

    @Query("UPDATE livechat SET isFav = 1 WHERE id = :id")
    void setMessageToBeFavorite(long id);

    @Delete
    void deleteMessage(LiveChat chat);

    @Query("DELETE FROM livechat")
    void deleteAll();
}

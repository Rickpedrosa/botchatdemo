package com.example.ricardopedrosarecupandroid.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.ricardopedrosarecupandroid.data.local.daos.LiveChatDao;
import com.example.ricardopedrosarecupandroid.data.local.daos.MessagesDao;
import com.example.ricardopedrosarecupandroid.data.local.entity.LiveChat;
import com.example.ricardopedrosarecupandroid.data.local.entity.MessageChat;

import java.util.concurrent.Executors;

@Database(entities = {MessageChat.class, LiveChat.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "recup_rick_pedrosa";
    private static volatile AppDatabase instance;

    private static AppDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(
                context.getApplicationContext(), AppDatabase.class,
                DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() ->
                                getInstance(context).messagesDao()
                                        .insertPredefinedBotMessages(MessageChat.populateData()));
                    }
                })
                .build();
    }

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = buildDatabase(context);
                }
            }
        }
        return instance;
    }

    public abstract MessagesDao messagesDao();
    public abstract LiveChatDao liveChatDao();
}
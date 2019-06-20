package com.example.ricardopedrosarecupandroid.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "message")
public class MessageChat {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "value")
    private String value;
    @ColumnInfo(name = "botOrUser")
    private int botOrUser;
    @ColumnInfo(name = "isFav")
    private boolean isFav;
    @ColumnInfo(name = "date_hour")
    private String date_hour;

    public MessageChat(long id, String value, int botOrUser, boolean isFav, String date_hour) {
        this.id = id;
        this.value = value;
        this.botOrUser = botOrUser;
        this.isFav = isFav;
        this.date_hour = date_hour;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getBotOrUser() {
        return botOrUser;
    }

    public void setBotOrUser(int botOrUser) {
        this.botOrUser = botOrUser;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public String getDate_hour() {
        return date_hour;
    }

    public void setDate_hour(String date_hour) {
        this.date_hour = date_hour;
    }

    public static MessageChat[] populateData() {
        return new MessageChat[]{
                new MessageChat(0, "Hello there!", 0, false, "12:30"),
                new MessageChat(0, "Cuentame mas por favor", 0, false, "12:33"),
                new MessageChat(0, "Claro que si wapi", 0, false, "12:36"),
                new MessageChat(0, "Baldomero did nothing wrong", 0, false, "12:39"),
                new MessageChat(0, "LOL XD LMAAAAOOO", 0, false, "12:43")
        };
    }
}

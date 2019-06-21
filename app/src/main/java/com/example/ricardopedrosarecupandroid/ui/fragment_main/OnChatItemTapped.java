package com.example.ricardopedrosarecupandroid.ui.fragment_main;

import com.example.ricardopedrosarecupandroid.data.local.entity.LiveChat;

public interface OnChatItemTapped {
    void updateItem(LiveChat liveChat, int position);
}

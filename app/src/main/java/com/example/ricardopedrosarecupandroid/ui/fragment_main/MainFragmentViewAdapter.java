package com.example.ricardopedrosarecupandroid.ui.fragment_main;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.ricardopedrosarecupandroid.R;
import com.example.ricardopedrosarecupandroid.base.BaseRecyclerViewAdapter;
import com.example.ricardopedrosarecupandroid.base.BaseViewHolder;
import com.example.ricardopedrosarecupandroid.data.local.entity.LiveChat;
import com.example.ricardopedrosarecupandroid.databinding.ChatItemBotBinding;
import com.example.ricardopedrosarecupandroid.databinding.ChatItemUserBinding;

public class MainFragmentViewAdapter extends BaseRecyclerViewAdapter<LiveChat,
        BaseViewHolder<LiveChat>> {

    private static final int BOT_TYPE = 0;
    private static final int USER_TYPE = 1;
    private OnChatItemTapped onChatItemTapped;
    private String favPreference;

    MainFragmentViewAdapter(OnChatItemTapped onChatItemTapped) {
        this.onChatItemTapped = onChatItemTapped;
        //setHasStableIds(true); <--- problema con el scrol tapaba items y daba mala info al ir haciabajo
    }

    @NonNull
    @Override
    public BaseViewHolder<LiveChat> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case BOT_TYPE:
                return new BotViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.chat_item_bot, parent, false));
            case USER_TYPE:
                return new UserViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.chat_item_user, parent, false));
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<LiveChat> holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getBotOrUser() == BOT_TYPE) {
            return BOT_TYPE;
        } else {
            return USER_TYPE;
        }
    }

    private void setImages(ImageView left, ImageView right, int position) {
        if (favPreference.equals("Before")) {
            if (getItem(position).isFav()) {
                left.setImageResource(R.drawable.ic_star_fav_24dp);
            } else {
                right.setImageResource(android.R.color.transparent);
            }
        } else {
            if (getItem(position).isFav()) {
                right.setImageResource(R.drawable.ic_star_fav_24dp);
            } else {
                left.setImageResource(android.R.color.transparent);
            }
        }
    }

    void setFavPreference(String favPreference) {
        this.favPreference = favPreference;
    }

    class BotViewHolder extends BaseViewHolder<LiveChat> {

        private ChatItemBotBinding b;

        BotViewHolder(ChatItemBotBinding itemView) {
            super(itemView.getRoot());
            b = itemView;
            b.cardLayoutChatBot.setOnClickListener(v -> onChatItemTapped.updateItem(
                    getItem(getAdapterPosition()),
                    getAdapterPosition()));
        }

        @Override
        public void bind(LiveChat type) {
            b.imgFavLeft.setImageResource(android.R.color.transparent);
            b.imgFavRight.setImageResource(android.R.color.transparent);
            b.lblChatContent.setText(type.getValue().concat(" ").concat(String.valueOf(getAdapterPosition())));
            b.lblChatDate.setText(type.getDate_hour());
            setImages(b.imgFavLeft, b.imgFavRight, getAdapterPosition());
        }
    }

    class UserViewHolder extends BaseViewHolder<LiveChat> {

        private ChatItemUserBinding b;

        UserViewHolder(ChatItemUserBinding itemView) {
            super(itemView.getRoot());
            b = itemView;
            b.cardLayoutChatUser.setOnClickListener(v -> onChatItemTapped.updateItem(
                    getItem(getAdapterPosition()),
                    getAdapterPosition()));
        }

        @Override
        public void bind(LiveChat type) {
            b.imgFavLeft.setImageResource(android.R.color.transparent);
            b.imgFavRight.setImageResource(android.R.color.transparent);
            b.lblChatContent.setText(type.getValue().concat(" ").concat(String.valueOf(getAdapterPosition())));
            b.lblChatDate.setText(type.getDate_hour());
            setImages(b.imgFavLeft, b.imgFavRight, getAdapterPosition());
        }
    }
}

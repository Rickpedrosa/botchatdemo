package com.example.ricardopedrosarecupandroid.ui.fragment_main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.ricardopedrosarecupandroid.R;
import com.example.ricardopedrosarecupandroid.base.BaseRecyclerViewAdapter;
import com.example.ricardopedrosarecupandroid.base.BaseViewHolder;
import com.example.ricardopedrosarecupandroid.data.local.entity.LiveChat;
import com.example.ricardopedrosarecupandroid.databinding.ChatItemBotBinding;
import com.example.ricardopedrosarecupandroid.databinding.ChatItemUserBinding;
import com.example.ricardopedrosarecupandroid.ui.main.MainActivityViewModel;

public class MainFragmentViewAdapter extends BaseRecyclerViewAdapter<LiveChat,
        BaseViewHolder<LiveChat>> {

    private MainActivityViewModel viewModel;
    private static final int BOT_TYPE = 0;
    private static final int USER_TYPE = 1;

    MainFragmentViewAdapter(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
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

    private void addMessageToFavorites(int botOrUser, int position, Context context) {
        LiveChat update = new LiveChat(
                getItem(position).getId(),
                getItem(position).getValue(),
                botOrUser,
                true,
                getItem(position).getDate_hour()
        );
        if (botOrUser == 0 && !getItem(position).isFav()) {
            viewModel.updateMessageLiveChat(update);
            Toast.makeText(context,
                    "Bot -> " +
                            getItem(position).getId() +
                            " added to favs",
                    Toast.LENGTH_SHORT).show();
        } else if (botOrUser == 1 && !getItem(position).isFav()) {
            viewModel.updateMessageLiveChat(update);
            Toast.makeText(context,
                    "User -> " +
                            getItem(position).getId() +
                            " added to favs",
                    Toast.LENGTH_SHORT).show();
        }
    }

    class BotViewHolder extends BaseViewHolder<LiveChat> {

        private ChatItemBotBinding b;

        BotViewHolder(ChatItemBotBinding itemView) {
            super(itemView.getRoot());
            b = itemView;
            b.cardLayoutChatBot.setOnClickListener(v -> addMessageToFavorites(
                    getItem(getAdapterPosition()).getBotOrUser(),
                    getAdapterPosition(),
                    b.cardLayoutChatBot.getContext()
            ));
        }

        @Override
        public void bind(LiveChat type) {
            b.lblChatContent.setText(type.getValue());
            b.lblChatDate.setText(type.getDate_hour());
            if (getItem(getAdapterPosition()).isFav()) {
                b.imgFavLeft.setImageResource(R.drawable.ic_star_fav_24dp);
                b.imgFavRight.setImageResource(android.R.color.transparent);
            } else {
                b.imgFavLeft.setImageResource(android.R.color.transparent);
                b.imgFavRight.setImageResource(android.R.color.transparent);
            }
        }
    }

    class UserViewHolder extends BaseViewHolder<LiveChat> {

        private ChatItemUserBinding b;

        UserViewHolder(ChatItemUserBinding itemView) {
            super(itemView.getRoot());
            b = itemView;
            b.cardLayoutChatUser.setOnClickListener(v -> addMessageToFavorites(
                    getItem(getAdapterPosition()).getBotOrUser(),
                    getAdapterPosition(),
                    b.cardLayoutChatUser.getContext()
            ));
        }

        @Override
        public void bind(LiveChat type) {
            b.lblChatContent.setText(type.getValue());
            b.lblChatDate.setText(type.getDate_hour());
            if (getItem(getAdapterPosition()).isFav()) {
                b.imgFavLeft.setImageResource(R.drawable.ic_star_fav_24dp);
                b.imgFavRight.setImageResource(android.R.color.transparent);
            }else {
                b.imgFavLeft.setImageResource(android.R.color.transparent);
                b.imgFavRight.setImageResource(android.R.color.transparent);
            }
        }
    }
}

package com.example.ricardopedrosarecupandroid.ui.fragment_main;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ricardopedrosarecupandroid.R;
import com.example.ricardopedrosarecupandroid.base.BaseRecyclerViewAdapter;
import com.example.ricardopedrosarecupandroid.data.local.entity.LiveChat;
import com.example.ricardopedrosarecupandroid.databinding.MainChatItemBinding;
import com.example.ricardopedrosarecupandroid.ui.main.MainActivityViewModel;

public class MainFragmentViewAdapter extends BaseRecyclerViewAdapter<LiveChat,
        MainFragmentViewAdapter.ViewHolder> {

    private MainActivityViewModel viewModel;

    MainFragmentViewAdapter(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
        //setHasStableIds(true); <--- problema con el scrol tapaba items y daba mala info al ir haciabajo
    }

    @NonNull
    @Override
    public MainFragmentViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.main_chat_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainFragmentViewAdapter.ViewHolder holder, int position) {
        holder.bind(getItem(holder.getAdapterPosition()));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private MainChatItemBinding b;

        ViewHolder(@NonNull MainChatItemBinding itemView) {
            super(itemView.getRoot());
            b = itemView;
//            b.cardLayoutChat.setOnClickListener(v -> {
//                if (!getItem(getAdapterPosition()).isFav()) {
//                    viewModel.updateMessageLiveChat(new LiveChat(
//                            getItem(getAdapterPosition()).getId(),
//                            getItem(getAdapterPosition()).getValue(),
//                            getItem(getAdapterPosition()).getBotOrUser(),
//                            true,
//                            getItem(getAdapterPosition()).getDate_hour()
//                    ));
//                    Toast.makeText(
//                            itemView.getRoot().getContext(),
//                            "Message " + String.valueOf(getItem(getAdapterPosition()).getId()) + " updated",
//                            Toast.LENGTH_SHORT).show();
//                }
//            });
        }

        void bind(LiveChat item) {
            b.lblChatContent.setText(item.getValue());
            b.lblChatDate.setText(item.getDate_hour());

            if (item.isFav()) {
                b.imgFavLeft.setImageResource(R.drawable.ic_star_black_24dp);
            } else {
                b.imgFavLeft.setImageResource(android.R.color.transparent);
            }

            if (item.getBotOrUser() == 1) {
                b.cardLayoutChat.setCardBackgroundColor(0xff2ecc71);
            } else {
                b.cardLayoutChat.setCardBackgroundColor(0xFFFFFFFF);
            }
        }
    }
}

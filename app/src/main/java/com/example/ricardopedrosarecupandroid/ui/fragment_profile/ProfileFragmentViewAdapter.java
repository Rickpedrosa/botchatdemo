package com.example.ricardopedrosarecupandroid.ui.fragment_profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.ricardopedrosarecupandroid.R;
import com.example.ricardopedrosarecupandroid.base.BaseRecyclerViewAdapter;
import com.example.ricardopedrosarecupandroid.base.BaseViewHolder;
import com.example.ricardopedrosarecupandroid.databinding.ProfileAnswerItemBinding;

public class ProfileFragmentViewAdapter extends
        BaseRecyclerViewAdapter<String, BaseViewHolder<String>> {
    @NonNull
    @Override
    public BaseViewHolder<String> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BotAnswerViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.profile_answer_item,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<String> holder, int position) {
        holder.bind(getItem(position));
    }

    class BotAnswerViewHolder extends BaseViewHolder<String> {

        private ProfileAnswerItemBinding b;

        BotAnswerViewHolder(ProfileAnswerItemBinding itemView) {
            super(itemView.getRoot());
            b = itemView;
        }

        @Override
        public void bind(String type) {
            b.botAnswer.setText(type);
        }
    }
}

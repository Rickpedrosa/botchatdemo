package com.example.ricardopedrosarecupandroid.base;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ricardopedrosarecupandroid.data.local.entity.LiveChat;

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
        //ButterKnife.bind(this, itemView);
    }

    public abstract void bind(T type);
}
package com.example.djangotestapp.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.djangotestapp.model.dataClass.Post

class DiffUtilCallBack : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id && oldItem.title.equals(newItem.title) && oldItem.category.id == newItem.category.id
    }
}
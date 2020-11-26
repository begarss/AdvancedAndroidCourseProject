package com.example.djangotestapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.djangotestapp.databinding.PostItemBinding
import com.example.djangotestapp.databinding.UsersPostItemBinding
import com.example.djangotestapp.model.dataClass.Post

class ViewPagerAdapter :RecyclerView.Adapter<ViewPagerPostHolder>(){
    private var items: ArrayList<Post> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerPostHolder {
        val dataBinding =
            UsersPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewPagerPostHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: ViewPagerPostHolder, position: Int) {
        when (holder) {
            is ViewPagerPostHolder -> {
                holder.bind(items.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(posts: ArrayList<Post>) {
        items = posts
        notifyDataSetChanged()
    }

    fun getList(): ArrayList<Post> {
        return items as ArrayList<Post>
    }
}
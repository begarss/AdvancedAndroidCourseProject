package com.example.djangotestapp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.djangotestapp.databinding.PostItemBinding
import com.example.djangotestapp.databinding.UsersPostItemBinding
import com.example.djangotestapp.model.dataClass.FavResponse
import com.example.djangotestapp.model.dataClass.Post

class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerPostHolder>() {
    private var items: ArrayList<Post> = ArrayList()

    private var favitems: ArrayList<FavResponse> = ArrayList()
    private var lookUserPosts = true
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerPostHolder {
        val dataBinding =
            UsersPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewPagerPostHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: ViewPagerPostHolder, position: Int) {
        when (holder) {
            is ViewPagerPostHolder -> {
                Log.d("UPP", "onBindViewHolder: $lookUserPosts")

                if (lookUserPosts) {
                    holder.bind(items.get(position))
                }
                else {
                    Log.d("UPP", "onBindViewHolder: $favitems")
                    holder.bind(favitems.get(position).post)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        if (lookUserPosts)
            return items.size
        else
            return favitems.size
    }

    fun submitList(posts: ArrayList<Post>) {
        items = posts
        lookUserPosts = true
        notifyDataSetChanged()
    }

    fun submitFavitems(favs: ArrayList<FavResponse>) {
        favitems = favs
        lookUserPosts = false
        notifyDataSetChanged()
    }

    fun getList(): ArrayList<Post> {
        return items as ArrayList<Post>
    }
}
package com.example.djangotestapp.ui.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.djangotestapp.viewmodel.PostViewModel
import com.example.djangotestapp.databinding.PostItemBinding
import com.example.djangotestapp.model.dataClass.Post
import com.example.djangotestapp.utils.DiffUtilCallBack
import kotlinx.android.synthetic.main.post_item.view.*

class PostAdapter internal constructor(
    private var listener: OnPostClickListener,
    private val postViewModel: PostViewModel
) : PagingDataAdapter<Post, PostViewHolder>(DiffUtilCallBack()) {

    private var items: ArrayList<Post> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val dataBinding =
            PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(dataBinding)
    }

//    override fun getItemCount(): Int {
//        return items.size
//    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, listener)}
//        when (holder) {
//            is PostViewHolder -> {
//                getItem(position)?.let {
//                    holder.bind(it, position)
//                }
//                Log.d("UPP", "onBindViewHolder: ${getItem(position)}")
//                holder.setIsRecyclable(false)
//                if (position % 2 == 0) {
//                    holder.itemView.clItem.setBackgroundColor(Color.parseColor("#df358d"))
//
//                } else {
//                    holder.itemView.clItem.setBackgroundColor(Color.parseColor("#724be6"));
//                }
//            }
//        }
    }

    fun submitList(postList: ArrayList<Post>) {
        items = postList
        notifyDataSetChanged()
//        refresh()
    }

    fun getList(): ArrayList<Post> {
        return items as ArrayList<Post>
    }

}

interface OnPostClickListener {
    fun onPostClick(post:Post)
}
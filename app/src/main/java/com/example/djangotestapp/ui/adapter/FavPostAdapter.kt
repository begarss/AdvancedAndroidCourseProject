package com.example.djangotestapp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.djangotestapp.databinding.PostItemBinding
import com.example.djangotestapp.databinding.UsersPostItemBinding
import com.example.djangotestapp.model.dataClass.FavResponse
import com.example.djangotestapp.model.dataClass.Post
import com.example.djangotestapp.viewmodel.PostViewModel


class FavPostAdapter internal constructor(
    private var listener: OnPostClickListener,
    private val postViewModel: PostViewModel
) : RecyclerView.Adapter<FavPostsViewHolder>() {
    private var favs:List<Int> = listOf()
    private var favitems: ArrayList<FavResponse> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavPostsViewHolder {
        val dataBinding =
            PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavPostsViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: FavPostsViewHolder, position: Int) {
        when (holder) {
            is FavPostsViewHolder -> {
                val markedPosts = favitems.map {
                    it.post_id
                }
                holder.bind(favitems.get(position).post,listener,markedPosts)

            }
        }
    }

    override fun getItemCount(): Int {

        return favitems.size
    }
    fun submitFavs(favList:List<Int>){
        favs = favList
    }

    fun submitFavitems(favs: ArrayList<FavResponse>) {
        favitems = favs
        notifyDataSetChanged()
    }

}

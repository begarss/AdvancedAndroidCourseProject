package com.example.djangotestapp.ui.adapter

import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.asLiveData
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.djangotestapp.BR
import com.example.djangotestapp.R
import com.example.djangotestapp.model.dataClass.FavResponse
import com.example.djangotestapp.model.dataClass.Post
import com.example.djangotestapp.utils.UserManager
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.post_item.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PostViewHolder internal constructor(
    private val dataBinding: ViewDataBinding
) : RecyclerView.ViewHolder(dataBinding.root) {
    //    private val listener = onPostClick
    private val date: TextView = itemView.tvDate
    private val markIcon: ImageButton = itemView.favIcon
    private lateinit var prefs: UserManager

    fun bind(post: Post, clickListener: OnPostClickListener, marked: List<Int>) {
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")
        val outputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
        val dater: Date = inputFormat.parse(post.date)
        val outputDateStr: String = outputFormat.format(dater)
        date.text = outputDateStr.toString()

        if (marked.contains(post.id)) {
            markIcon.setImageResource(R.drawable.ic_bookmark)

            Log.d("UPP", "bind: $marked and ${post.title} ")
        } else {
            markIcon.setImageResource(R.drawable.icon_fav_normal)

        }

        markIcon.setOnClickListener {
            if (marked.contains(post.id)) {
                markIcon.setImageResource(R.drawable.icon_fav_normal)
                clickListener.removeLike(postId = post.id)
                Log.d("UPP", "bind: $marked and ${post.title} ")
            } else {
                markIcon.setImageResource(R.drawable.ic_bookmark)
                clickListener.setLike(post.id)
            }
        }

        dataBinding.setVariable(BR.Post, post)


        dataBinding.executePendingBindings()
        itemView.setOnClickListener {
            clickListener.onPostClick(post)
        }
    }


}
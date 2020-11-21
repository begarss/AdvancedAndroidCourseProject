package com.example.djangotestapp.ui.adapter

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.djangotestapp.BR
import com.example.djangotestapp.model.dataClass.Post
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

    fun bind(post: Post, clickListener: OnPostClickListener) {
        Log.d("UPP", "bind: $post")
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")
        val outputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
        val dater: Date = inputFormat.parse(post.date)
        val outputDateStr: String = outputFormat.format(dater)
        date.text = outputDateStr.toString()
        dataBinding.setVariable(BR.Post, post)


        dataBinding.executePendingBindings()
        itemView.setOnClickListener{
            clickListener.onPostClick(post)
        }
    }

//    override fun onClick(v: View?) {
//        if (v != null) {
//            listener.onPostClick(adapterPosition, v)
//        }
//    }

}
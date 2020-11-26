package com.example.djangotestapp.ui.adapter

import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.djangotestapp.BR
import com.example.djangotestapp.model.dataClass.Post
import kotlinx.android.synthetic.main.post_item.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ViewPagerPostHolder internal constructor(
    private val dataBinding:ViewDataBinding
):RecyclerView.ViewHolder(dataBinding.root){
    private val date: TextView = itemView.tvDate

    fun bind(post: Post) {
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")
        val outputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
        val dater: Date = inputFormat.parse(post.date)
        val outputDateStr: String = outputFormat.format(dater)
        date.text = outputDateStr.toString()
        dataBinding.setVariable(BR.Post, post)


        dataBinding.executePendingBindings()

    }
}
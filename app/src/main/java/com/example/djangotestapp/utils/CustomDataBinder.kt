package com.example.djangotestapp.utils

import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("profileImage")
fun setAva(view: CircleImageView, url: String?) {
    if (!url.isNullOrEmpty() && url != "null") {
        Glide.with(view.context).load(url).into(view)

    }
}
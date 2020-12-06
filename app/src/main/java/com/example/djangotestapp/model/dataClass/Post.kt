package com.example.djangotestapp.model.dataClass

import com.example.djangotestapp.model.dataClass.Author
import com.example.djangotestapp.model.dataClass.Category

data class Post(
    val author: Author,
    val category: Category,
    var date: String,
    val description: String,
    val id: Int,
    val is_published: Boolean,
    val title: String
)
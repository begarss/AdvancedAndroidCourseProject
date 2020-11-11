package com.example.djangotestapp.model.dataClass

data class PostCreateBody(
    val author_id: Int,
    val category_id: Int,
    val description: String,
    val title: String,
    val is_published:Boolean
)
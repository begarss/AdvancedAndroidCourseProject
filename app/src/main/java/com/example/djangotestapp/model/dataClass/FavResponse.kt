package com.example.djangotestapp.model.dataClass

data class FavResponse(
    val author_id: Int,
    val id: Int,
    val is_favorite: Boolean,
    val post_id: Int
)
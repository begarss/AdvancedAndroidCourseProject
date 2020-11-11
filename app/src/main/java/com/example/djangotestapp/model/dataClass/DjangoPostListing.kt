package com.example.djangotestapp.model.dataClass

data class DjangoPostListing(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<Post>
)
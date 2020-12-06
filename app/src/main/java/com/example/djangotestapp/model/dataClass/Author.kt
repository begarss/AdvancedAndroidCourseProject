package com.example.djangotestapp.model.dataClass

data class Author(
    val email: String,
    val id: Int,
    val is_superuser: Boolean,
    val username: String,
    var profile_pic: String
)
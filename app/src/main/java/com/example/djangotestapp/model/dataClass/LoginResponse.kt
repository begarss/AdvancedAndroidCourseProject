package com.example.djangotestapp.model.dataClass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResponse(
    val is_superuser: Boolean,
    val profile_pic: String,
    val token: String,
    val userid: Int,
    val username: String
) : Parcelable
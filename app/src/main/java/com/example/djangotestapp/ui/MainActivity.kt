package com.example.djangotestapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.djangotestapp.R
import com.example.djangotestapp.model.api.API
import com.example.djangotestapp.model.dataClass.Post
import com.example.djangotestapp.model.dataClass.PostCreateBody
import retrofit2.Call
import retrofit2.Callback


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, PostListFragment()).commit()


    }



}
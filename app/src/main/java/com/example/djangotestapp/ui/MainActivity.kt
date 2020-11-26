package com.example.djangotestapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.djangotestapp.R
import com.example.djangotestapp.model.api.API
import com.example.djangotestapp.model.dataClass.Post
import com.example.djangotestapp.model.dataClass.PostCreateBody
import com.example.djangotestapp.utils.UserManager
import com.example.djangotestapp.utils.startNewActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, PostListFragment()).commit()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavView)
        val navController = findNavController(R.id.fragment)

        bottomNavigationView.setupWithNavController(navController)

        val userPreferences = UserManager(this)
        userPreferences.authToken.asLiveData().observe(this, Observer {
            if (it == null)
                startNewActivity(LoginActivity::class.java)
        })

    }



}
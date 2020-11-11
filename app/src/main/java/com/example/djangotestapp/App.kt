package com.example.djangotestapp

import android.app.Application
import com.example.djangotestapp.di.networkModule
import com.example.djangotestapp.di.postModule
import com.example.djangotestapp.di.viewModelModule
import org.koin.core.context.startKoin

class App:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            modules(networkModule, postModule, viewModelModule)
        }
    }
}
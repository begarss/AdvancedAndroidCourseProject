package com.example.djangotestapp.model.api

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object API {

    const val BASE_URL = "http://192.168.1.253:8000/api/"
    val tempUrl = "http://192.168.61.66:8000/api/"
    private var retrofit: Retrofit? = null
    private var mPostListService: PostListService? = null
    private val postListService: PostListService = generatePostService()

    fun generatePostService(): PostListService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL).client(getHttpClient()).addCallAdapterFactory(
                RxJava2CallAdapterFactory.create()
            ).addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
        return retrofit.create(PostListService::class.java)
    }

    private fun getHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(getLogginInterceptor())
            .build()
    }

    private fun getLogginInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(logger = object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d("UPP", "log: $message")
            }

        }).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }


    }
}
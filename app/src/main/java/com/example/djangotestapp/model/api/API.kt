package com.example.djangotestapp.model.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object API {

    const val BASE_URL = "http://192.168.1.253:8000/api/"
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
        return OkHttpClient.Builder()
            .build()
    }

//    fun getClient(): Retrofit? {
//        if (retrofit == null) {
//            retrofit = Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//        }
//        return retrofit
//    }

//    fun getApi(): PostListService? {
//        mPostListService = getClient()
//            ?.create(PostListService::class.java)
//        return mPostListService
//    }
}
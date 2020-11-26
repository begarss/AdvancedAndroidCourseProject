package com.example.djangotestapp.model.api

import androidx.lifecycle.LiveData
import com.example.djangotestapp.model.dataClass.*
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface PostListService {

    @GET("posts")
    suspend fun getListPost(
        @Query("page") page: Int? = null
    ): Response<DjangoPostListing>

    @Headers("Content-Type:application/json;")
    @POST("posts/")
    fun addPost(@Body post: PostCreateBody): Observable<Post>

    @GET("categories")
    fun getCategoryList(): Observable<List<Category>>

    @GET("categories/{id}/posts/")
    fun getCategoryPosts(@Path("id") id: Int): Observable<List<Post>>

    @GET("posts/{id}/")
    fun getPosttDetails(@Path("id") id: Int): Single<Post>

    //User
    @Headers("Content-Type:application/json;")
    @POST("users/")
    suspend fun createUser(@Body user: UserCreateBody): Response<Author>

    @Headers("Content-Type:application/json;")
    @POST("login/")
    suspend fun login(@Body login: UserCreateBody): LoginResponse

    @GET("profile/{id}/posts")
    suspend fun getUserPosts(@Path("id") id:Int): List<Post>
}
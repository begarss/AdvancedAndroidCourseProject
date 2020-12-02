package com.example.djangotestapp.model.api

import androidx.lifecycle.LiveData
import com.example.djangotestapp.model.dataClass.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
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
    suspend fun getCategoryPosts(
        @Path("id") id: Int,
        @Query("page") page: Int? = null
    ): Response<DjangoPostListing>

    @GET("posts/{id}/")
    fun getPosttDetails(@Path("id") id: Int): Single<Post>


    //User
    @Headers("Content-Type:application/json;")
    @POST("users/")
    suspend fun createUser(@Body user: UserCreateBody): Author

    @Headers("Content-Type:application/json;")
    @POST("login/")
    suspend fun login(@Body login: UserCreateBody): LoginResponse

    @GET("profile/{id}/posts")
    suspend fun getUserPosts(@Path("id") id: Int): List<Post>

    @Multipart
    @PUT("users/{id}/profile/")
    suspend fun setAvatar(@Path("id") id: Int, @Part filePart: MultipartBody.Part): ProfileResponse

    @PUT("users/{id}/")
    suspend fun editUserInfo(@Path("id") id: Int, @Body info: UserCreateBody): Author
    //favorites posts
    @GET("fav/{user_id}/")
    suspend fun getFavPosts(@Path("user_id") id: Int): List<FavResponse>

    @POST("fav/")
    suspend fun markAsFav(@Body postInfo: FavBody): FavResponse

    @DELETE("fav/{user_id}/{post_id}/")
    suspend fun removeFromFav(@Path("user_id") userId: Int, @Path("post_id") post_id: Int)
}
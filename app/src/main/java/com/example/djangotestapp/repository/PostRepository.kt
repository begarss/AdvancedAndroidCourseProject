package com.example.djangotestapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.djangotestapp.model.api.PostListService
import com.example.djangotestapp.model.dataClass.Post
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow

class PostRepository(private val postListService: PostListService) {

    fun fetchPosts(): Flow<PagingData<Post>> {
        // 3
        return Pager(
            PagingConfig(pageSize = 6, enablePlaceholders = false,prefetchDistance = 3)
        ) {
            PagingSource(postListService)
        }.flow
    }

//    fun getPosts(next:String,previous:String,onResult: (isSuccess: Boolean, response: List<Post>) -> Unit){
//        val posts = postListService.getListPost(next = next,previous =previous )
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({posts->
//                onResult(true,posts)
//                Log.i("MG", "success + $posts")
//            },{
//                onResult(false, emptyList())
//                Log.i("MG", it.message)
//            })
//    }

    fun getCategories(onResult: (isSuccess: Boolean, response: List<String>) -> Unit){
        val categories = postListService.getCategoryList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({categories->
                onResult(true,categories.map { it.catName })
                Log.i("MG", "success + $categories")
            },{
                onResult(false, emptyList())
                Log.i("MG", it.message)
            })
    }

    fun getCategoryPosts(id:Int,onResult: (isSuccess: Boolean, response: List<Post>) -> Unit){
        val posts = postListService.getCategoryPosts(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({posts->
                onResult(true,posts)
                Log.i("MG", "CATSPOSTS + $posts")
            },{
                onResult(false, emptyList())
                Log.i("MG", it.message)
            })
    }

    fun getPost(id:Int,onResult: (isSuccess: Boolean, response: Post?) -> Unit) {
        val students = postListService.getPosttDetails(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ post ->
                onResult(true, post)
                Log.i("MG", "success + $post")

            }, {
                it.printStackTrace()
                onResult(false, null)
                Log.i("MG", it.message)

            })
    }

}
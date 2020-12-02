package com.example.djangotestapp.viewmodel

import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.djangotestapp.model.api.Resource
import com.example.djangotestapp.model.dataClass.*
import com.example.djangotestapp.repository.PostRepository
import com.example.djangotestapp.utils.UserManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent

class PostViewModel(private val repository: PostRepository) : BaseViewModel(), KoinComponent {
    val postList = MutableLiveData<List<Post>>()
    val categoryPosts = MutableLiveData<List<Post>>()
    val categoryList = MutableLiveData<List<String>>()
    val post = MutableLiveData<Post>()
    val favPosts = MutableLiveData<Resource<List<FavResponse>>>()

    fun fetchPosts(): Flow<PagingData<Post>> {
        val das = repository.fetchPosts().map {
            it.map { it }
                .map {
                    it
                }.toString()
        }
        Log.d(
            "UPP", "fetchPosts from viewmodel: $das "
        )
        return repository.fetchPosts().cachedIn(viewModelScope)
    }

    fun getCatPosts(id:Int):Flow<PagingData<Post>>{
        return repository.getCatPosts(id).cachedIn(viewModelScope)
    }
    fun getFavorites(userId:Int) = viewModelScope.launch {
        favPosts.value = repository.getFavs(userId)
    }

    fun addToFav(post: FavBody) = viewModelScope.launch {
        repository.addToFav(post)
    }

    fun deleteFav(userId: Int,postId:Int) = viewModelScope.launch {
        repository.removeFav(userId, postId)
    }
    fun getPrefs(): UserManager {
        return repository.getPrefs()
    }

    fun getCategories() {
        repository.getCategories { isSuccess, response ->
            if (isSuccess) {
                categoryList.value = response
                empty.value = false
            } else {
                categoryList.value = emptyList()
                empty.value = true
            }
        }
    }



    fun fetchPostDetails(id: Int) {
        dataLoading.value = true
        repository.getPost(id) { isSuccess, response ->
            dataLoading.value = false
            if (isSuccess) {
                post.value = response
                empty.value = false
            } else
                empty.value = true
        }
    }

    val user = MutableLiveData<LoginResponse>()


}
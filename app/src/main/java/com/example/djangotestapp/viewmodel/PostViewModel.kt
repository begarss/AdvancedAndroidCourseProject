package com.example.djangotestapp.viewmodel

import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.djangotestapp.model.dataClass.Category
import com.example.djangotestapp.model.dataClass.Post
import com.example.djangotestapp.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import org.koin.core.KoinComponent

class PostViewModel(private val repository: PostRepository) : BaseViewModel(), KoinComponent {
    val postList = MutableLiveData<List<Post>>()
    val categoryPosts = MutableLiveData<List<Post>>()
    val categoryList = MutableLiveData<List<String>>()
    val post = MutableLiveData<Post>()

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

//    fun getPosts() {
//        dataLoading.value = true
//        repository.getPosts { isSuccess, response ->
//            dataLoading.value = false
//            if (isSuccess) {
//                postList.value = response
//                empty.value = false
//            } else {
//                postList.value = emptyList()
//                empty.value = true
//            }
//        }
//    }

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

    fun getCatPosts(id: Int) {
        repository.getCategoryPosts(id) { isSuccess, response ->
            dataLoading.value = false
            if (isSuccess) {
                categoryPosts.value = response
                empty.value = false
            } else {
                categoryPosts.value = emptyList()
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
}
package com.example.djangotestapp.viewmodel

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.djangotestapp.model.api.Resource
import com.example.djangotestapp.model.dataClass.Author
import com.example.djangotestapp.model.dataClass.LoginResponse
import com.example.djangotestapp.model.dataClass.Post
import com.example.djangotestapp.repository.UserRepository
import com.example.djangotestapp.ui.LoginActivity
import com.example.djangotestapp.utils.UserManager
import com.example.djangotestapp.utils.startNewActivity
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent


class UserViewModel(private val repository: UserRepository) : AndroidViewModel(Application()),
    KoinComponent {
    private val _newUserResponse: MutableLiveData<Resource<Author>> = MutableLiveData()
    val newUserResponse: LiveData<Resource<Author>>
        get() = _newUserResponse

    private val _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>>
        get() = _loginResponse

    val postList = MutableLiveData<Resource<List<Post>>>()
    var postCount=MutableLiveData<Int>()


//    suspend fun createUser(username: String, password: String) {
//        viewModelScope.launch {
//            repository.createUser(username, password) { isSuccess, response ->
//                if (isSuccess) {
//                    newUser.value = response
//                } else {
//                    newUser.value = null
//                }
//            }
//        }
//    }

    fun createUser(username: String, password: String) = viewModelScope.launch {
        _newUserResponse.value = repository.creatUser(username, password)
    }


    fun login(username: String, password: String) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.login(username, password)
    }

    fun saveUserInfo(token: String, name: String, id: Int, ava: String, issuper: Boolean) =
        viewModelScope.launch {

            repository.saveUserInfo(token, name, id, ava, issuper)
        }

    fun savePostsCount(size:Int) = viewModelScope.launch {
        repository.saveUserPostsCount(size)
    }

    fun getUserPosts(id: Int) = viewModelScope.launch {
        postList.value = repository.getUserPosts(id)
    }

    fun getPrefs(): UserManager {
        return repository.getPrefs()
    }

}
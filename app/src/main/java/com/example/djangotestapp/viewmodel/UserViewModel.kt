package com.example.djangotestapp.viewmodel

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.djangotestapp.model.api.Resource
import com.example.djangotestapp.model.dataClass.*
import com.example.djangotestapp.repository.UserRepository
import com.example.djangotestapp.ui.LoginActivity
import com.example.djangotestapp.utils.UserManager
import com.example.djangotestapp.utils.startNewActivity
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.koin.core.KoinComponent


class UserViewModel(private val repository: UserRepository) : AndroidViewModel(Application()),
    KoinComponent {
    private val _newUserResponse: MutableLiveData<Resource<Author>> = MutableLiveData()
    val newUserResponse: LiveData<Resource<Author>>
        get() = _newUserResponse

    private val _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>>
        get() = _loginResponse

    private val _editResponse: MutableLiveData<Resource<Author>> = MutableLiveData()
    val editResponse: LiveData<Resource<Author>>
        get() = _editResponse

    val favPosts = MutableLiveData<Resource<List<FavResponse>>>()

    val postList = MutableLiveData<Resource<List<Post>>>()
    var postCount = MutableLiveData<Int>()


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

    fun savePostsCount(size: Int) = viewModelScope.launch {
        repository.saveUserPostsCount(size)
    }

    fun saveUserAva(uri: String) = viewModelScope.launch {
        repository.saveUserAva(uri)
    }

    fun saveUserName(name: String) = viewModelScope.launch {
        repository.saveUserName(name)
    }

    fun getUserPosts(id: Int) = viewModelScope.launch {
        postList.value = repository.getUserPosts(id)
    }

    fun setUserAva(id: Int, file: MultipartBody.Part) = viewModelScope.launch {
        repository.setAvatar(id, file)
    }

    fun editUser(id: Int,info:UserCreateBody) = viewModelScope.launch {

        _editResponse.value = repository.editUser(id,info)
    }

    fun getPrefs(): UserManager {
        return repository.getPrefs()
    }

    fun getFavorites(userId:Int) = viewModelScope.launch {
        favPosts.value = repository.getFavs(userId)
    }

}
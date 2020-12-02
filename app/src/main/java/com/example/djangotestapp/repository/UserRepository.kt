package com.example.djangotestapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.djangotestapp.model.api.PostListService
import com.example.djangotestapp.model.dataClass.Author
import com.example.djangotestapp.model.dataClass.FavResponse
import com.example.djangotestapp.model.dataClass.LoginResponse
import com.example.djangotestapp.model.dataClass.UserCreateBody
import com.example.djangotestapp.ui.LoginActivity
import com.example.djangotestapp.utils.UserManager
import com.example.djangotestapp.utils.startNewActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import retrofit2.Response
import java.util.prefs.Preferences
import kotlin.math.log

class UserRepository(private val service: PostListService, private val manager: UserManager) : BaseRepository() {



    suspend fun creatUser(username: String,password: String) = safeApiCall{
        service.createUser(UserCreateBody(username, password))
    }

    suspend fun login(username: String, password: String) = safeApiCall {
        service.login(UserCreateBody(username, password))
    }

    suspend fun saveUserInfo(token: String,name:String,id:Int,ava:String,issuper:Boolean) {
        manager.saveAuthToken(token)
        manager.saveUserName(name)
        manager.saveUserId(id)
        manager.saveUserAva(ava)
        manager.saveUserSuperState(issuper)
    }

    suspend fun saveUserPostsCount(size:Int){
        manager.saveUserPostsCount(size)
    }

    suspend fun saveUserAva(uri : String){
        manager.saveUserAva(uri)
    }
    suspend fun saveUserName(name: String){
        manager.saveUserName(name)
    }

    suspend fun getUserPosts(id:Int) = safeApiCall {

        service.getUserPosts(id)
    }
    fun getPrefs():UserManager{
        return manager
    }

    suspend fun setAvatar(id: Int,file: MultipartBody.Part)=safeApiCall {
        service.setAvatar(id,file)
    }

    suspend fun editUser(id:Int,info:UserCreateBody) = safeApiCall {
        service.editUserInfo(id,info)
    }



    suspend fun getFavs(userId:Int) = safeApiCall {
        service.getFavPosts(userId)
    }

    suspend fun removeFav(userId: Int,postId:Int) = safeApiCall {
        service.removeFromFav(userId,postId)
    }
}


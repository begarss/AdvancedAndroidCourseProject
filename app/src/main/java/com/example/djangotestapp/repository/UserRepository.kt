package com.example.djangotestapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.djangotestapp.model.api.PostListService
import com.example.djangotestapp.model.dataClass.Author
import com.example.djangotestapp.model.dataClass.LoginResponse
import com.example.djangotestapp.model.dataClass.UserCreateBody
import com.example.djangotestapp.ui.LoginActivity
import com.example.djangotestapp.utils.UserManager
import com.example.djangotestapp.utils.startNewActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.prefs.Preferences
import kotlin.math.log

class UserRepository(private val service: PostListService, private val manager: UserManager) : BaseRepository() {

//    suspend fun createUser(
//        username: String,
//        password: String,
//        onResult: (isSuccess: Boolean, response: Author?) -> Unit
//    ) {
//        try {
//            val res = service.createUser(UserCreateBody(username, password))
//            if (res.isSuccessful) {
//                res.body()?.let { onResult(true, it) }
//                Log.d("UPP", "createUser: ${res.body()?.username}")
//            }
//
//        } catch (
//            cause: Throwable
//        ) {
//            onResult(false, null)
//
//            throw UserRefreshError("Error with user registration", cause)
//        }
//    }

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



    suspend fun getUserPosts(id:Int) = safeApiCall {

        service.getUserPosts(id)
    }
    fun getPrefs():UserManager{
        return manager
    }
}

class UserRefreshError(message: String, cause: Throwable?) : Throwable(message, cause)

interface UserRefreshCallback {
    fun onSuccess()
    fun onError(cause: Throwable)
}
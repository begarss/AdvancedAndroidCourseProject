package com.example.djangotestapp.di

import android.content.Context
import android.os.UserManager
import androidx.datastore.core.DataStore
import com.example.djangotestapp.viewmodel.PostViewModel
import com.example.djangotestapp.model.api.API
import com.example.djangotestapp.repository.PostRepository
import com.example.djangotestapp.repository.UserRepository
import com.example.djangotestapp.viewmodel.UserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single { API.generatePostService() }
}
val postModule = module {
    single { PostRepository(get()) }
}

val userModule = module {
    single { getUM(androidContext()) }
    factory { UserRepository(get(),manager = get()) }
    viewModel { UserViewModel(get()) }
}

val viewModelModule = module {
    viewModel { PostViewModel(get()) }
}

private fun getUM(context: Context): com.example.djangotestapp.utils.UserManager {
    return com.example.djangotestapp.utils.UserManager(context)
}





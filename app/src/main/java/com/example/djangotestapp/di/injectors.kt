package com.example.djangotestapp.di

import com.example.djangotestapp.viewmodel.PostViewModel
import com.example.djangotestapp.model.api.API
import com.example.djangotestapp.repository.PostRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single { API.generatePostService() }
}
val postModule = module {
    single { PostRepository(get()) }
}

val viewModelModule = module {
    viewModel { PostViewModel(get()) }
}
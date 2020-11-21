package com.example.djangotestapp.repository

import android.util.Log
import com.example.djangotestapp.model.api.PostListService
import androidx.paging.PagingSource
import com.example.djangotestapp.model.dataClass.Post
import retrofit2.HttpException
import java.io.IOException


class PagingSource(private val postListService: PostListService) : PagingSource<Int, Post>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        return try {
            val nextPageNumber = params.key ?: 1

            val response = postListService.getListPost(nextPageNumber)
            Log.d("UPP", "load: ${response.body()}")
            val listing = response.body()
            val posts = listing?.results
            val prevKey = if (nextPageNumber > 1) nextPageNumber - 1 else null
            val nextKey = if (!listing?.next.isNullOrEmpty() && !listing?.next.equals("null")) nextPageNumber + 1 else null
            Log.d("UPP", "load: ${posts}")

            LoadResult.Page(
                posts ?: listOf(),
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

}
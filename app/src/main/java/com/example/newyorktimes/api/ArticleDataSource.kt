package com.example.newyorktimes.api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.newyorktimes.model.Article
import com.example.newyorktimes.model.Articles
import com.example.newyorktimes.utils.Event
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class ArticleDataSource @Inject constructor(private val apiService: ApiService) :
    PageKeyedDataSource<Int, Article>() {

    companion object {
        private const val NETWORK_ERROR = "No Network to fetch request"
        private const val REQUEST_FAILED = "API Failed to give response"
        private const val NEW_YORK_TIMES = "NewYorkTimes"
    }
    var errorMessage : MutableLiveData<Event<String>> = MutableLiveData()
    private var queryString: String? = null

    fun setQuery(query: String) {
        queryString = query
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Article>
    ) {
        try {
            queryString?.let { query ->
                val call: Call<Articles> = apiService.getArticleList(0, query)
                val result: Response<Articles> = call.execute()
                if (result.isSuccessful) {
                    result.body()?.response?.docs?.let { articles ->
                        callback.onResult(articles, -1, 1)
                    }
                } else {
                    errorMessage.postValue(Event(REQUEST_FAILED))
                    Log.d(NEW_YORK_TIMES, REQUEST_FAILED)
                }
            }
        } catch (exception: IOException) {
            errorMessage.postValue(Event(NETWORK_ERROR))
            Log.d(NEW_YORK_TIMES, NETWORK_ERROR)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
        try {
            queryString?.let { query ->
                val call: Call<Articles> = apiService.getArticleList(params.key, query)
                val result: Response<Articles> = call.execute()
                if (result.isSuccessful) {
                    result.body()?.response?.docs?.let { articles ->
                        callback.onResult(articles, params.key + 1)
                    }
                } else {
                    Log.d(NEW_YORK_TIMES, REQUEST_FAILED)
                }
            }
        } catch (exception: IOException) {
            Log.d(NEW_YORK_TIMES, NETWORK_ERROR)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
        try {
            queryString?.let { query ->
                val call: Call<Articles> = apiService.getArticleList(params.key, query)
                val result: Response<Articles> = call.execute()
                if (result.isSuccessful) {
                    result.body()?.response?.docs?.let { articles ->
                        callback.onResult(articles, if (params.key > 1) params.key - 1 else 0)
                    }
                } else {
                    Log.d(NEW_YORK_TIMES, REQUEST_FAILED)
                }
            }
        } catch (exception: IOException) {
            Log.d(NEW_YORK_TIMES, NETWORK_ERROR)
        }
    }

}
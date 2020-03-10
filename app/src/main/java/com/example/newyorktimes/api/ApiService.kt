package com.example.newyorktimes.api

import com.example.newyorktimes.model.Articles
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("svc/search/v2/articlesearch.json")
    fun getArticleList(
        @Query("page") page: Int,
        @Query("q") query: String
    ): Call<Articles>
}
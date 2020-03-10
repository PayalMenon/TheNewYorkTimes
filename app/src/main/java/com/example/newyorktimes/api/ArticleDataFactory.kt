package com.example.newyorktimes.api

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.newyorktimes.model.Article
import javax.inject.Inject

class ArticleDataFactory @Inject constructor(private val articleDataSource: ArticleDataSource) :
    DataSource.Factory<Int, Article>() {

    val articleDataSourceLiveData = MutableLiveData<ArticleDataSource>()

    fun setQueryString(query: String) {
        articleDataSource.setQuery(query)
    }

    override fun create(): DataSource<Int, Article> {
        articleDataSourceLiveData.postValue(articleDataSource)
        return articleDataSource
    }

}
package com.example.newyorktimes.viewmodel

import android.app.Application
import android.view.animation.Transformation
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.newyorktimes.api.ArticleDataFactory
import com.example.newyorktimes.api.ArticleDataSource
import com.example.newyorktimes.model.Article
import com.example.newyorktimes.utils.Event
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    application: Application,
    val articleDataFactory: ArticleDataFactory
) : AndroidViewModel(application) {

    //LiveData
    private val _showLoading = MutableLiveData<Event<Unit>>()
    val showLoading: LiveData<Event<Unit>>
        get() = _showLoading

    private val _hideLoading = MutableLiveData<Event<Unit>>()
    val hideLoading: LiveData<Event<Unit>>
        get() = _hideLoading

    private val _hideText = MutableLiveData<Event<Unit>>()
    val hideText: LiveData<Event<Unit>>
        get() = _hideText

    private val _showText = MutableLiveData<Event<Int>>()
    val showText: LiveData<Event<Int>>
        get() = _showText

    private val _showArticleList = MutableLiveData<Event<PagedList<Article>>>()
    val showArticleList: LiveData<Event<PagedList<Article>>>
        get() = _showArticleList

    private val _showArticleDetailsFragment = MutableLiveData<Event<Article>>()
    val showArticleDetailsFragment: LiveData<Event<Article>>
        get() = _showArticleDetailsFragment

    private val _launchShareEmail = MutableLiveData<Event<String>>()
    val launchShareEmail: LiveData<Event<String>>
        get() = _launchShareEmail

    var articleLiveData: LiveData<PagedList<Article>> ? =null

    var errorMessage : LiveData<Event<String>> = Transformations.switchMap<ArticleDataSource, Event<String>>(articleDataFactory.articleDataSourceLiveData, ArticleDataSource::errorMessage)

    fun onSearchSelected(searchQuery: String) {
        _hideText.value = Event(Unit)
        _showLoading.value = Event(Unit)
        articleDataFactory.setQueryString(searchQuery)
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(20).build()
        articleLiveData = LivePagedListBuilder<Int, Article>(articleDataFactory, pagedListConfig).build()
    }

    fun onListFetched(list : PagedList<Article>) {
        _hideLoading.value = Event(Unit)
        _showArticleList.value = Event(list)
    }

    fun onArticleSelected(article: Article) {
        _showArticleDetailsFragment.value = Event(article)
    }

    fun getArticleList() : PagedList<Article>? {
        return articleLiveData?.value
    }

    fun getDisplayData(date: String, format : String, pattenRequired: String) : String? {
        val time : Date? = SimpleDateFormat(format, Locale.US).parse(date)
        time?.let { parsedDate ->
            return SimpleDateFormat(pattenRequired, Locale.US).format(parsedDate)
        }
        return null
    }

    fun shareEmail(articleUrl : String) {
        _launchShareEmail.value = Event(articleUrl)
    }
}
package com.example.newyorktimes.ui

import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.newyorktimes.R
import com.example.newyorktimes.model.Article
import com.example.newyorktimes.viewmodel.SearchViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.search_fragment.view.*
import javax.inject.Inject


class ArticleListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var searchViewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        searchViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(SearchViewModel::class.java)
        val view = inflater.inflate(R.layout.search_fragment, container, false)
        setArticleList(view)
        return view
    }

    private fun setArticleList(view: View) {
        view.article_list.visibility = View.VISIBLE
        val adapter = ArticleAdapter() { article: Article -> onArticleClicked(article) }
        view.article_list.adapter = adapter
        adapter.submitList(searchViewModel.getArticleList())
    }

    private fun onArticleClicked(article: Article) {
        searchViewModel.onArticleSelected(article)
    }

}
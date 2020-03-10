package com.example.newyorktimes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.newyorktimes.R
import com.example.newyorktimes.model.Article
import com.example.newyorktimes.viewmodel.SearchViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.list_fragment.view.*
import javax.inject.Inject


class ArticleListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var adapter : ArticleAdapter
    private lateinit var navController : NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        searchViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(SearchViewModel::class.java)
        val view = inflater.inflate(R.layout.list_fragment, container, false)
        adapter = ArticleAdapter { article: Article -> onArticleClicked(article) }
        view.article_list.adapter = adapter
        setArticleList(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
    }

    private fun setArticleList(view: View) {
        view.article_list.visibility = View.VISIBLE
        adapter.submitList(searchViewModel.getArticleList())
    }

    private fun onArticleClicked(article: Article) {
        val destination = ArticleListFragmentDirections.navigateToDetailFragment(article)
        navController.navigate(destination)
    }
}
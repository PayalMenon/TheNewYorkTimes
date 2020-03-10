package com.example.newyorktimes.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.newyorktimes.R
import com.example.newyorktimes.utils.EventObserver
import com.example.newyorktimes.viewmodel.SearchViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.search_fragment.view.*
import javax.inject.Inject

class SearchFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        searchViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(SearchViewModel::class.java)
        val view = inflater.inflate(R.layout.search_fragment, container, false)
        this.setHasOptionsMenu(true)
        setListener(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
    }
    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchMenuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchMenuItem.actionView as SearchView
        searchView.queryHint = resources.getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchViewModel.onSearchSelected(query)
                    setListObserver()
                }
                return true
            }
        })
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    private fun setListener(view: View) {
        searchViewModel.showLoading.observe(this, EventObserver {
            view.loading.visibility = View.VISIBLE
            view.loading_text.visibility = View.VISIBLE
        })

        searchViewModel.hideLoading.observe(this, EventObserver {
            view.loading.visibility = View.GONE
            view.loading_text.visibility = View.GONE
        })

        searchViewModel.hideText.observe(this, EventObserver {
            view.info_text.visibility = View.GONE
        })

        searchViewModel.errorMessage.observe(this, EventObserver{
            view.info_text.text = resources.getString(R.string.oops_message)
            view.info_text.visibility = View.VISIBLE
        })

        searchViewModel.showArticleList.observe(this, EventObserver {
            val destination = SearchFragmentDirections.navigateToListFragment()
            navController.navigate(destination)
        })

    }

    private fun setListObserver() {
        searchViewModel.articleLiveData?.observe(this, Observer { list ->
            searchViewModel.onListFetched(list)
        })
    }
}
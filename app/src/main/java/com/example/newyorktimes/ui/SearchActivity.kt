package com.example.newyorktimes.ui

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.EXTRA_TEXT
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.newyorktimes.R
import com.example.newyorktimes.model.Article
import com.example.newyorktimes.utils.EventObserver
import com.example.newyorktimes.viewmodel.SearchViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.search_activity.*
import javax.inject.Inject

class SearchActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var searchViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        searchViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)
        setListener()
    }

    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
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
        return super.onCreateOptionsMenu(menu)
    }

    private fun setListener() {
        searchViewModel.showLoading.observe(this, EventObserver {
            this.loading.visibility = View.VISIBLE
            this.loading_text.visibility = View.VISIBLE
        })

        searchViewModel.hideLoading.observe(this, EventObserver {
            this.loading.visibility = View.GONE
            this.loading_text.visibility = View.GONE
        })

        searchViewModel.hideText.observe(this, EventObserver {
            this.info_text.visibility = View.GONE
        })

        searchViewModel.showArticleList.observe(this, EventObserver { list ->
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, ArticleListFragment(), "search")
                .commit()
        })

        searchViewModel.showArticleDetailsFragment.observe(this, EventObserver { article ->
            startDetailsFragment(article)
        })

        searchViewModel.launchShareEmail.observe(this, EventObserver { webUrl ->
            val intent = Intent(ACTION_SEND).apply { putExtra(EXTRA_TEXT, webUrl) }
            startActivity(Intent.createChooser(intent, resources.getString(R.string.send_email)))
        })

        searchViewModel.errorMessage.observe(this, EventObserver{ errorMessage ->
            this.info_text.text = resources.getString(R.string.oops_message)
            this.info_text.visibility = View.VISIBLE
        })
    }

    private fun setListObserver() {
        searchViewModel.articleLiveData?.observe(this, Observer { list ->
            searchViewModel.onListFetched(list)
        })
    }

    private fun startDetailsFragment(article : Article) {
        val bundle = Bundle()
        bundle.putParcelable("article", article)
        val fragment = ArticleDetailsFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}

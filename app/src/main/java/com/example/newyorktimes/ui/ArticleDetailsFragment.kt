package com.example.newyorktimes.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.newyorktimes.R
import com.example.newyorktimes.model.Article
import com.example.newyorktimes.model.MultiMedia
import com.example.newyorktimes.utils.EventObserver
import com.example.newyorktimes.viewmodel.SearchViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.detail_fragment.view.*
import javax.inject.Inject

class ArticleDetailsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var searchViewModel: SearchViewModel

    companion object {
        private const val ARTICLE = "article"
        private const val PUBLISHED_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
        private const val PUBLISHED_DATE_REQUIRED_PATTERN = "MMM. dd, yyyy"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        searchViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(
            SearchViewModel::class.java
        )
        this.setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.detail_fragment, container, false)
        arguments?.getParcelable<Article>(ARTICLE)?.let { article ->
            updateDetailsScreen(view, article)
        }
        setListener()
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                arguments?.getParcelable<Article>(ARTICLE)?.let { article ->
                    searchViewModel.shareEmail(article.webUrl)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_search)?.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    private fun updateDetailsScreen(view: View, article: Article) {
        view.detail_title.text = article.headline.headlineMain
        view.detail_abstract.text = article.abstract
        view.detail_author.text = article.author.author
        view.detail_paragraph.text = article.leadParagraph
        val imageUrl: List<MultiMedia> =
            article.multimedia.filter { it.type == ArticleAdapter.IMAGE_TYPE }
        if (imageUrl.isNotEmpty()) {
            Glide.with(view.context).load(ArticleAdapter.IMAGE_BASE_URL.plus(imageUrl[0].imageUrl))
                .into(view.detail_image)
        }
        val date: String? = searchViewModel.getDisplayData(
            article.publishedOn,
            PUBLISHED_DATE_FORMAT,
            PUBLISHED_DATE_REQUIRED_PATTERN
        )
        date?.let {
            view.detail_published_date.text = date
        }
    }

    private fun setListener() {

        searchViewModel.launchShareEmail.observe(this, EventObserver { webUrl ->
            val intent = Intent(Intent.ACTION_SEND).apply { putExtra(Intent.EXTRA_TEXT, webUrl) }
            startActivity(Intent.createChooser(intent, resources.getString(R.string.send_email)))
        })
    }
}
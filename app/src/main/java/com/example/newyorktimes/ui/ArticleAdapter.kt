package com.example.newyorktimes.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newyorktimes.R
import com.example.newyorktimes.model.Article
import com.example.newyorktimes.model.MultiMedia
import com.example.newyorktimes.ui.ArticleAdapter.Companion.IMAGE_BASE_URL
import com.example.newyorktimes.ui.ArticleAdapter.Companion.IMAGE_TYPE
import kotlinx.android.synthetic.main.article_item.view.*

class ArticleAdapter(val clickListner: (Article) -> Unit) :
    PagedListAdapter<Article, ArticleViewHolder>(ArticleDiffUtil()) {

    companion object {
        const val IMAGE_BASE_URL = "https://www.nytimes.com/"
        const val IMAGE_TYPE = "image"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.article_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        getItem(position)?.let { article ->
            holder.setArticle(article, clickListner)
        }
    }
}

class ArticleViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun setArticle(article: Article, clickListner: (Article) -> Unit) {
        view.article_title.text = article.headline.headlineMain
        val imageUrl: List<MultiMedia> = article.multimedia.filter { it.type == IMAGE_TYPE }
        if (imageUrl.isNotEmpty()) {
            Glide.with(view.context).load(IMAGE_BASE_URL.plus(imageUrl[0].imageUrl))
                .into(view.article_image)
        }
        view.article_parent.setOnClickListener { clickListner(article) }
    }
}
package com.example.newyorktimes.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class Articles(
    val response: Docs
)

data class Docs(
    val docs: List<Article>
)

@Parcelize
data class Article(
    @SerializedName("_id")
    val id: String,
    val headline: Headline,
    val uri: String,
    @SerializedName("web_url")
    val webUrl: String,
    @SerializedName("subsection_name")
    val subSectionName: String,
    @SerializedName("lead_paragraph")
    val leadParagraph: String,
    val abstract: String,
    @SerializedName("pub_date")
    val publishedOn: String,
    val multimedia: List<MultiMedia>,
    @SerializedName("byline")
    val author: Author
) : Parcelable

@Parcelize
data class Headline(
    @SerializedName("main")
    val headlineMain: String
) : Parcelable

@Parcelize
data class MultiMedia(
    val type: String,
    @SerializedName("url")
    val imageUrl: String,
    val subType: String
) : Parcelable

@Parcelize
data class Author(
    @SerializedName("original")
    val author: String
) : Parcelable


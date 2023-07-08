package com.example.linksdemo.data

import com.google.gson.annotations.SerializedName
import java.util.Date

data class DashboardData(

    @SerializedName("recent_links")
    val recentLink : List<Link>,

    @SerializedName("top_links")
    val topLink : List<Link>,

    @SerializedName("overall_url_chart")
    val overallUrlChart : Map<Date, Int>
)

data class Link(

    @SerializedName("url_id")
    val urlId: Int,

    @SerializedName("web_link")
    val webLink: String,

    @SerializedName("smart_link")
    val smartLink: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("total_clicks")
    val totalClicks: Int,

    @SerializedName("original_image")
    val originalImage: String,

    @SerializedName("thumbnail")
    val thumbnail: String?,

    @SerializedName("times_ago")
    val timesAgo: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("domain_id")
    val domainId: String,

    @SerializedName("url_prefix")
    val urlPrefix: String?,

    @SerializedName("app")
    val app: String
)

data class Data(
    val date: Date,
    val totalClicks: Int
)
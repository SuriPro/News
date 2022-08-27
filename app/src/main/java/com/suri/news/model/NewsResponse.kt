package com.suri.news.model

data class NewsResponse(
    val category: String,
    val data: List<News>,
)
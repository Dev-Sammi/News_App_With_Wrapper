package com.example.mvvm_news_app.modules

import com.example.mvvm_news_app.modules.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)
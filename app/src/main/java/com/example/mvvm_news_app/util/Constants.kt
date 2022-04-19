package com.example.mvvm_news_app.util

import com.example.mvvm_news_app.BuildConfig

class Constants {

    companion object{
        const val API_KEY_CONST = BuildConfig.API_KEY
        const val BASE_URL = "https://newsapi.org/"
        const val SEARCH_NEWS_TIME_DELAY = 500L
    }
}
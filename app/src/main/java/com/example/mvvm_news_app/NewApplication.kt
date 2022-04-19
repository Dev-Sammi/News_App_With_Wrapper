package com.example.mvvm_news_app

import android.app.Application
import com.example.mvvm_news_app.repository.NewsRepository

class NewApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        NewsRepository.initialize(this)
    }
}
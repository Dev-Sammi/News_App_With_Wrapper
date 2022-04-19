package com.example.mvvm_news_app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm_news_app.repository.NewsRepository
import java.lang.IllegalArgumentException


class NewsViewModelProviderFactory(private val newRepository: NewsRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(newRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
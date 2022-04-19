package com.example.mvvm_news_app.repository

import android.content.Context
import com.example.mvvm_news_app.api.RetrofitInstance
import com.example.mvvm_news_app.db.ArticleDatabase
import com.example.mvvm_news_app.modules.Article

class NewsRepository(context: Context) {

    private val database = ArticleDatabase.getInstance(context)
    private val newsApi = RetrofitInstance.api



    //  Api functions
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        newsApi.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        newsApi.searchForNews(searchQuery, pageNumber)

    //    Database Functions
    suspend fun upsert(article: Article) = database.getArticleDao().upsert(article)

    fun getSavedNews() = database.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = database.getArticleDao().deleteArticle(article)


    companion object {
        private var REPOSITORY_INSTANCE: NewsRepository? = null
        fun initialize(context: Context) {
            if (REPOSITORY_INSTANCE == null) {
                REPOSITORY_INSTANCE = NewsRepository(context)
            }
        }

        fun get(): NewsRepository {
            return REPOSITORY_INSTANCE
                ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}
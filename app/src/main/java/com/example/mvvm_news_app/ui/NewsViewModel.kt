package com.example.mvvm_news_app.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm_news_app.modules.Article
import com.example.mvvm_news_app.modules.NewsResponse
import com.example.mvvm_news_app.repository.NewsRepository
import com.example.mvvm_news_app.util.WrapperClass
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.Response

private const val TAG = "NewsViewModel"
class NewsViewModel(val newsRepository: NewsRepository) : ViewModel() {

    private val _articleEventChannel = Channel<ArticleEvent>()
    val articleEvent = _articleEventChannel.receiveAsFlow()

    private var _breakingNews: MutableLiveData<WrapperClass<NewsResponse>> = MutableLiveData()
    val breakingNews: LiveData<WrapperClass<NewsResponse>> = _breakingNews
    val breakingNewsPage = 1

    private var _searchNews: MutableLiveData<WrapperClass<NewsResponse>> = MutableLiveData()
    val searchNews: LiveData<WrapperClass<NewsResponse>> = _searchNews
    val searchNewsPage = 1

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        _breakingNews.value = WrapperClass.Loading()
        val apiResponse = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
        _breakingNews.value = handleBreakingNewsResponse(apiResponse)
    }


    fun searchNews(searchQuery: String) =
        viewModelScope.launch {
            _searchNews.value = WrapperClass.Loading()
            val apiResponse = newsRepository.searchNews(searchQuery, searchNewsPage)
            _searchNews.value = handleSearchNewsResponse(apiResponse)
        }

    //Import Response from Retrofit below in this method
    private fun handleBreakingNewsResponse(apiResponse: Response<NewsResponse>): WrapperClass<NewsResponse> {
        if(apiResponse.isSuccessful){
            apiResponse.body()?.let { resultResponse ->
                Log.d(TAG, "handleSearchNewsResponse: ${resultResponse.articles.size}")
                return WrapperClass.Success(resultResponse)
            }
        }
        return WrapperClass.Error(apiResponse.message())
    }

    private fun handleSearchNewsResponse(apiResponse: Response<NewsResponse>): WrapperClass<NewsResponse> {
        if(apiResponse.isSuccessful){
            apiResponse.body()?.let { resultResponse ->
                Log.d(TAG, "handleSearchNewsResponse: ${resultResponse.articles.size}")
                return WrapperClass.Success(resultResponse)
            }
        }
        return WrapperClass.Error(apiResponse.message())
    }

    fun savedArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
        _articleEventChannel.send(ArticleEvent.ShowDeleteArticleMessage(article))
    }

    sealed class ArticleEvent(){
        data class ShowDeleteArticleMessage(val article: Article): ArticleEvent()
    }

}
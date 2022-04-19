package com.example.mvvm_news_app.util

sealed class WrapperClass<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : WrapperClass<T>(data)
    class Error<T>(message: String, data: T? = null) : WrapperClass<T>(data, message)
    class Loading<T> : WrapperClass<T>()
}
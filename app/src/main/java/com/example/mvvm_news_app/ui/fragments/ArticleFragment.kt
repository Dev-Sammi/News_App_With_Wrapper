package com.example.mvvm_news_app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.example.mvvm_news_app.R
import com.example.mvvm_news_app.databinding.FragmentArticleBinding
import com.example.mvvm_news_app.repository.NewsRepository
import com.example.mvvm_news_app.ui.NewsViewModel
import com.example.mvvm_news_app.ui.NewsViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect

class ArticleFragment() : Fragment() {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        _binding = FragmentArticleBinding.bind(view)

        val repository = NewsRepository.get()
        val factory = NewsViewModelProviderFactory(repository)
        val viewModel: NewsViewModel by activityViewModels { factory }
        this.viewModel = viewModel

        val args by navArgs<ArticleFragmentArgs>()
        val articles = args.article

        binding.apply {
            fab.setOnClickListener {
                viewModel.savedArticle(articles)
                Snackbar.make(requireView(), "Article saved successfully", Snackbar.LENGTH_LONG)
                    .show()
            }
            webView.apply {
                webViewClient = WebViewClient()
                loadUrl(articles.url)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
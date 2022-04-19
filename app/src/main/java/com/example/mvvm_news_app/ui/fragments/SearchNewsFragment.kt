package com.example.mvvm_news_app.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm_news_app.R
import com.example.mvvm_news_app.adapters.NewsAdapter
import com.example.mvvm_news_app.databinding.FragmentSearchNewsBinding
import com.example.mvvm_news_app.modules.Article
import com.example.mvvm_news_app.repository.NewsRepository
import com.example.mvvm_news_app.ui.NewsViewModel
import com.example.mvvm_news_app.ui.NewsViewModelProviderFactory
import com.example.mvvm_news_app.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.mvvm_news_app.util.WrapperClass
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "SearchNewsFragment"

class SearchNewsFragment() : Fragment(R.layout.fragment_search_news),
    NewsAdapter.OnArticleClickListener {


    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchNewsBinding.bind(view)

        val repository = NewsRepository.get()
        val factory = NewsViewModelProviderFactory(repository)
        val viewModel: NewsViewModel by activityViewModels { factory }
        this.viewModel = viewModel

        newsAdapter = NewsAdapter(this)

        binding.apply {
            var job: Job? = null
            etSearch.addTextChangedListener { editable ->
                job?.cancel()
                job = MainScope().launch {
                    delay(SEARCH_NEWS_TIME_DELAY)
                    editable?.let {
                        viewModel.searchNews(editable.toString())
                    }
                }


            }

            rvSearchNews.apply {
                adapter = newsAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL
                    )
                )
            }

        }


        //observe searchNews in viewModel
        viewModel.searchNews.observe(viewLifecycleOwner) { wrapperClass ->
            when (wrapperClass) {
                is WrapperClass.Loading -> {
                    binding.paginationProgressBar.visibility = View.VISIBLE
                }
                is WrapperClass.Success -> {
//                    hideProgressbar()
                    binding.paginationProgressBar.visibility = View.INVISIBLE
                    wrapperClass.data?.let {
                        Log.d(TAG, "onViewCreated: ${it.articles.size}")
                        newsAdapter.differ.submitList(it.articles)
                    }
                }
                is WrapperClass.Error -> {
                    wrapperClass.message?.let {
                        Log.e(TAG, "An error occurred $it")
                    }
                }

            }

        }
    }

//    private fun hideProgressbar() {
//        binding.paginationProgressBar.visibility = View.INVISIBLE
//    }


    override fun onArticleClick(article: Article) {
//        val action =
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

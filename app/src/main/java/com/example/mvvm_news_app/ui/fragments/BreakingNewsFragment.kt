package com.example.mvvm_news_app.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_news_app.R
import com.example.mvvm_news_app.adapters.NewsAdapter
import com.example.mvvm_news_app.databinding.FragmentBreakingNewsBinding
import com.example.mvvm_news_app.modules.Article
import com.example.mvvm_news_app.repository.NewsRepository
import com.example.mvvm_news_app.ui.NewsViewModel
import com.example.mvvm_news_app.ui.NewsViewModelProviderFactory
import com.example.mvvm_news_app.util.WrapperClass
import retrofit2.Response

private const val TAG = "BreakingNewsFragment"

class BreakingNewsFragment() : Fragment(R.layout.fragment_breaking_news),
    NewsAdapter.OnArticleClickListener {

    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBreakingNewsBinding.bind(view)

        val repository = NewsRepository.get()
        val factory = NewsViewModelProviderFactory(repository)
        val viewModel: NewsViewModel by activityViewModels { factory }
        this.viewModel = viewModel

        newsAdapter = NewsAdapter(this)

        binding.apply {

            rvBreakingNews.apply {
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

        viewModel.breakingNews.observe(viewLifecycleOwner) { wrapperClass ->
            when (wrapperClass) {
                is WrapperClass.Loading -> {
                    binding.paginationProgressBar.visibility = View.VISIBLE
                }
                is WrapperClass.Success -> {
//                    hideProgressbar()
                    binding.paginationProgressBar.visibility = View.INVISIBLE
                    wrapperClass.data?.let {
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
        val action = BreakingNewsFragmentDirections.actionBreakingNewsFragment2ToArticleFragment2(article)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
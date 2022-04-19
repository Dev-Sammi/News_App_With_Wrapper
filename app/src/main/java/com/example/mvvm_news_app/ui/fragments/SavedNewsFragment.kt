package com.example.mvvm_news_app.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_news_app.R
import com.example.mvvm_news_app.adapters.NewsAdapter
import com.example.mvvm_news_app.databinding.FragmentArticleBinding
import com.example.mvvm_news_app.databinding.FragmentBreakingNewsBinding
import com.example.mvvm_news_app.databinding.FragmentSavedNewsBinding
import com.example.mvvm_news_app.modules.Article
import com.example.mvvm_news_app.repository.NewsRepository
import com.example.mvvm_news_app.ui.NewsViewModel
import com.example.mvvm_news_app.ui.NewsViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment() : Fragment(R.layout.fragment_saved_news),
    NewsAdapter.OnArticleClickListener {

    private var _binding: FragmentSavedNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var viewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSavedNewsBinding.bind(view)

        val repository = NewsRepository.get()
        val factory = NewsViewModelProviderFactory(repository)
        val viewModel: NewsViewModel by activityViewModels { factory }
        this.viewModel = viewModel

        newsAdapter = NewsAdapter(this)

        viewModel.getSavedNews().observe(viewLifecycleOwner) {
            newsAdapter.differ.submitList(it)
        }

        binding.apply {
            rvSavedNews.apply {
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

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.bindingAdapterPosition
                    val article = newsAdapter.differ.currentList[position]
                    viewModel.deleteArticle(article)
                }
            }).attachToRecyclerView(rvSavedNews)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.articleEvent.collect { event ->
                when (event) {
                    is NewsViewModel.ArticleEvent.ShowDeleteArticleMessage -> {
                        Snackbar.make(
                            requireView(),
                            "Article successfully delete",
                            Snackbar.LENGTH_LONG
                        )
                            .setAction("Undo") {
                                viewModel.savedArticle(event.article)
                            }.show()
                    }
                }
            }
        }


    }

    override fun onArticleClick(article: Article) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


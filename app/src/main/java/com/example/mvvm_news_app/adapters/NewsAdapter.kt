package com.example.mvvm_news_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvm_news_app.R
import com.example.mvvm_news_app.databinding.ItemArticlePreviewBinding
import com.example.mvvm_news_app.modules.Article

class NewsAdapter(
    private  val listener : OnArticleClickListener
    ) : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    //diffCallback
    private val diffCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding =
            ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.bind(article)

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    //viewHolder
    inner class ArticleViewHolder(private val binding: ItemArticlePreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if(position != RecyclerView.NO_POSITION){
                    val article = differ.currentList[position]
                    if (article != null){
                        listener.onArticleClick(article)
                    }
                }
            }

        }


        fun bind(article: Article?) {
            if (article !== null) {
                binding.apply {
                    Glide.with(itemView)
                        .load(article.urlToImage)
                        .error(R.drawable.ic_error)
                        .into(ivArticleImage)

                    tvTitle.text = article.title
                    tvSource.text = article.source.name
                    tvDescription.text = article.description
                    tvPublishedAt.text = article.publishedAt

                }
            }
        }
    }

    interface OnArticleClickListener{
        fun onArticleClick(article: Article)
    }



}


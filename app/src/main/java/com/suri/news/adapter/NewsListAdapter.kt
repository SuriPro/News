package com.suri.news.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.suri.news.activity.NewsActivity
import com.suri.news.databinding.NewsItemBinding
import com.suri.news.model.News


class NewsListAdapter(private var list: List<News>, var newsActivity: NewsActivity) :
    RecyclerView.Adapter<NewsListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.rootView.setOnClickListener{
            newsActivity.onNewsClicked(list[position].url)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(it: List<News>) {
        list=it
    }


    fun addData(it: List<News>) {

    }

    inner class MyViewHolder(private val binding: NewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: News) {
            binding.news = item
        }

    }

}
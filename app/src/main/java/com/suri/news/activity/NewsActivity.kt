package com.suri.news.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.suri.news.R
import com.suri.news.adapter.NewsListAdapter
import com.suri.news.databinding.ActivityNewsBinding
import com.suri.news.view_model.MyViewModelFactory
import com.suri.news.view_model.NewsViewModel

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    lateinit var viewModel: NewsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_news)

        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(this, "NEWS_VM")
        ).get(NewsViewModel::class.java)

        viewModel.getError().observe(this, Observer {
            Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
        })

        val adapter = NewsListAdapter(emptyList(),this)
        binding.recycle.adapter = adapter

        viewModel.news.observe(this) {
            if(it!=null) {
                adapter.setData(it)
                adapter.notifyDataSetChanged()
            }
        }
        binding.viewModel=viewModel
    }

    fun onNewsClicked(url: String) {
        val intent=Intent(this,WebviewActivity::class.java)
        intent.putExtra("URL",url)
        startActivity(intent)
    }
}
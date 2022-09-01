package com.suri.news.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.filter
import com.suri.news.R
import com.suri.news.adapter.NewsListAdapter
import com.suri.news.adapter.NewsLoadStateAdapter
import com.suri.news.databinding.ActivityNewsBinding
import com.suri.news.view_model.MyViewModelFactory
import com.suri.news.view_model.NewsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    lateinit var viewModel: NewsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news)

        binding.txtDate.text = SimpleDateFormat("EEEE dd, MMM yyyy", Locale.getDefault()).format(Date())

        val adapter = NewsListAdapter(this)
        binding.recycle.adapter = adapter.withLoadStateFooter(
            NewsLoadStateAdapter()
        )


        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(this, "NEWS_VM", adapter)
        )[NewsViewModel::class.java]

        lifecycleScope.launch {
            viewModel.news.collect {
                adapter.submitData(it)
            }
        }

        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val str=s.toString()
                lifecycleScope.launch {
                    viewModel.news.collect {

                        adapter.submitData(if (str.isEmpty()) it else it.filter { news ->
                            news.title.contains(str, true) or news.author.contains(str,true)
                        })

                    }
                }
            }
        })

        binding.viewModel = viewModel
    }

    fun onNewsClicked(url: String) {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("URL", url)
        startActivity(intent)
    }
}
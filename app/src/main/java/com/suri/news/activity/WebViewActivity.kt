package com.suri.news.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.suri.news.R
import com.suri.news.databinding.ActivityWebviewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding : ActivityWebviewBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_webview)

        if(intent.extras!=null){
            val url=intent.extras?.getString("URL") ?: "www.google.com"
            binding.webview.loadUrl(url)
        }

    }

    override fun onBackPressed() {
        if(binding.webview.canGoBack()){
            binding.webview.goBack()
        }else{
            super.onBackPressed()
        }
    }

}
package com.suri.news.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.suri.news.R

class Utils {
    companion object {
        @JvmStatic
        @BindingAdapter("loadImage")
        fun loadImage(view: ImageView, url: String?) {
            if (!url.isNullOrEmpty()) {
                Glide.with(view.context).load(url).placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder).into(view)
            }
        }

    }
}
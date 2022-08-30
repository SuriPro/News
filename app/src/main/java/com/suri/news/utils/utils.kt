package com.suri.news.utils

import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

class Utils {
    companion object {
        @JvmStatic
        @BindingAdapter("loadImage")
        fun loadImage(view: ImageView, url: String?) {
            if (!url.isNullOrEmpty()) {
                Glide.with(view.context).load(url).into(view)
            }
        }

        @JvmStatic
        @BindingAdapter("collapseText")
        fun collapseText(view: TextView, text: String) {
            if (text.length>100) {
                view.text = Html.fromHtml(text.substring(
                    0,
                    100
                ) + "<b><u>View More</u></b>")

                view.setOnClickListener{
                    view.text=text
                }
            }else{
                view.text=text
            }
        }

    }
}
package com.suri.news.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class MyViewModelFactory(
    private val context: Context,
    val tag: String,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (tag) {
            "NEWS_VM" -> NewsViewModel(context) as T
            else -> null as T
        }
    }
}
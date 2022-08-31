package com.suri.news.view_model

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.suri.news.adapter.NewsListAdapter
import com.suri.news.adapter.NewsPagingSource
import com.suri.news.database.AppDatabase
import com.suri.news.model.NewsResponse
import com.suri.news.repo.NewsRepository
import com.suri.news.restapi.ApiClient
import com.suri.news.restapi.ApiService
import com.suri.news.utils.NetworkUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class NewsViewModel(private val context: Context, private val adapter: NewsListAdapter) :
    ViewModel() {

    val error: ObservableField<String> = ObservableField()

    val isLoading: ObservableBoolean = ObservableBoolean(false)

    private val database = AppDatabase.getDatabase(context)
    private val newsRepository = NewsRepository(database.newsDao())

    var news = Pager(
        PagingConfig(
            pageSize = 10,
            initialLoadSize = 10
        ),
    ) {
        NewsPagingSource(database.newsDao())
    }.flow.cachedIn(viewModelScope)


    init {
        getNews()
    }

    private fun getNews() {
        val count = newsRepository.getNewsCount(
            SimpleDateFormat("dd MMM yyyy,EEEE", Locale.getDefault()).format(
                Date()
            )
        )
        if (count == 0) {
            if (NetworkUtils(context).isNetworkAvailable()) {
                isLoading.set(true)

                //delete previous news
                newsRepository.deleteAll()
                val request = ApiClient.buildService(ApiService::class.java)
                val disposable = CompositeDisposable()

                disposable.add(
                    request.getNewsByCategory("all")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::handleResponse, this::handleError)
                )

            } else
                error.set("Check Internet Connection")
        }

    }

    private fun handleError(throwable: Throwable) {
        Log.e("error", "" + throwable.message)
        error.set(throwable.message)
        isLoading.set(false)
    }

    private fun handleResponse(response: NewsResponse) {
        //database insertion
        newsRepository.insertNews(response.data)
        adapter.refresh()
        isLoading.set(false)
    }


}
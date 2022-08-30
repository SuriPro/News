package com.suri.news.view_model

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.filter
import com.suri.news.adapter.NewsListAdapter
import com.suri.news.adapter.NewsPagingSource
import com.suri.news.database.AppDatabase
import com.suri.news.model.News
import com.suri.news.model.NewsResponse
import com.suri.news.repo.NewsRepository
import com.suri.news.restapi.ApiClient
import com.suri.news.restapi.ApiService
import com.suri.news.utils.NetworkUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

class NewsViewModel(private val context: Context, private val adapter: NewsListAdapter) : ViewModel() {

    private val request = ApiClient.buildService(ApiService::class.java)
    private val disposable = CompositeDisposable()

    private val error = MutableLiveData<String>()
    fun getError(): LiveData<String> = error

    val isLoading: ObservableBoolean = ObservableBoolean(false)

    private val database = AppDatabase.getDatabase(context)
    private val newsRepository = NewsRepository(database.newsDao())

    var data = Pager(
        PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            initialLoadSize = 10,
        ),
    ) {
        NewsPagingSource(database.newsDao())
    }.flow.cachedIn(viewModelScope)


    init {
        getNews()
    }

    private fun getNews() {
        val count = newsRepository.getNewsCount()
        if (count==0) {
            if (NetworkUtils(context).isNetworkAvailable()) {
                isLoading.set(true)

                disposable.add(
                    request.getNewsByCategory("all")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::handleResponse, this::handleError)
                )
            } else
                error.value = "Check Internet Connection"
        }

    }

    private fun handleError(throwable: Throwable) {
        Log.e("error",""+throwable.message)
        error.value = throwable.message
        isLoading.set(false)
    }

    private fun handleResponse(response: NewsResponse) {
        //database insertion
        print(response.data.size)
        newsRepository.insertNews(response.data)
        isLoading.set(false)
        adapter.refresh()
    }


}
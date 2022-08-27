package com.suri.news.view_model

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

class NewsViewModel constructor(private val context: Context) : ViewModel() {

    private val request = ApiClient.buildService(ApiService::class.java)
    private val disposable = CompositeDisposable()

    private val error = MutableLiveData<String>()
    fun getError(): LiveData<String> = error

    val isLoading: ObservableBoolean = ObservableBoolean(false)

    lateinit var tempNews: MutableList<News>
    var news: MutableLiveData<List<News>> = MutableLiveData()

    val database = AppDatabase.getDatabase(context)
    private val newsRepository = NewsRepository(database.newsDao())

    var page = 1

    init {
        getNews()
    }

    fun onSearchTextChanged(
        s: CharSequence, start: Int, before: Int,
        count: Int
    ) {
        Log.e("EditText Changed", "" + s)
        if (s.isNotEmpty()) {

            //new by me
            news.value = tempNews.filter {
                it.title.contains(s, true) or it.author.contains(s, true)
            }

        } else {
            news.value = tempNews
        }

    }

    private fun getNews() {
        tempNews = newsRepository.getNews()
        if (tempNews.isEmpty()) {
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
        } else {
            news.value = tempNews
            isLoading.set(false)
        }


    }

    private fun handleError(throwable: Throwable) {
        error.value = throwable.message
        isLoading.set(false)
    }

    private fun handleResponse(response: NewsResponse) {
        //database insertion
        newsRepository.insertNews(response.data)
        isLoading.set(true)
        if(response.data.isNotEmpty())
            getNews();
    }


}
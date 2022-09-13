package com.suri.news.restapi

import com.suri.news.model.NewsResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {

    @GET("news")
    fun getNewsByCategory(@Query("category") category: String): Flowable<NewsResponse>

}
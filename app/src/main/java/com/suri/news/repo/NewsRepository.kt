package com.suri.news.repo

import android.util.Log
import com.suri.news.dao.NewsDao
import com.suri.news.model.News


class NewsRepository(private val newsDao: NewsDao) {

    fun insertNews(news: List<News>) {
        newsDao.insertNews(news)
    }

    fun getNewsCount(date:String):  Int {
        Log.e("date",date)
        return newsDao.getNewsCount(date)
    }

    fun deleteAll() {
        newsDao.deleteAll()
    }
}
package com.suri.news.repo

import com.suri.news.dao.NewsDao
import com.suri.news.model.News


class NewsRepository(private val newsDao: NewsDao) {

    fun insertNews(news: List<News>) {
        newsDao.insertNews(news)
    }

    fun getNewsCount():  Int {
        return newsDao.getNewsCount()
    }
}
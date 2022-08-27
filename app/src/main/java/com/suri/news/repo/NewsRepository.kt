package com.suri.news.repo

import com.suri.news.dao.NewsDao
import com.suri.news.model.News


class NewsRepository(private val newsDao: NewsDao) {

    fun insertNews(user: List<News>) {
        newsDao.insertNews(user)
    }

    fun getNews(): MutableList<News> {
        return newsDao.getNews()
    }
}
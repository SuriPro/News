package com.suri.news.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.suri.news.model.News

@Dao
interface NewsDao {

    @Insert(onConflict = REPLACE)
    fun insertNews(user: List<News>)

    @Query("SELECT * FROM News")
    fun getNews(): MutableList<News>

}
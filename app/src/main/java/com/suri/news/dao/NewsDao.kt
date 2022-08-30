package com.suri.news.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.suri.news.model.News

@Dao
interface NewsDao {

    @Insert(onConflict = REPLACE)
    fun insertNews(news: List<News>)

    @Query("SELECT * FROM News LIMIT :limit OFFSET :offset")
    fun getNews(limit : Int, offset : Int,): MutableList<News>

    @Query("SELECT COUNT(id) FROM News ")
    fun getNewsCount(): Int

}
package com.suri.news.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.suri.news.model.News

@Dao
interface NewsDao {

    @Insert(onConflict = REPLACE)
    fun insertNews(news: List<News>)

    @Query("SELECT * FROM News LIMIT :limit OFFSET (:page * :limit)")
    fun getNews(limit : Int, page : Int,): MutableList<News>

    @Query("SELECT COUNT(id) FROM News WHERE date=:date")
    fun getNewsCount(date:String): Int

    @Query("DELETE FROM News")
    fun deleteAll()

}
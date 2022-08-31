package com.suri.news.adapter

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.suri.news.dao.NewsDao
import com.suri.news.model.News
import kotlinx.coroutines.delay

class NewsPagingSource(
    private val newsDao: NewsDao,
) : PagingSource<Int, News>() {

    override fun getRefreshKey(state: PagingState<Int, News>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {
        val page = params.key ?: 0

        return try {
            Log.e("PARAMS","${params.loadSize} / $page")
            val items = newsDao.getNews(
                params.loadSize,
                page
            )

            //page loading shows purpose
            if (page != 0) delay(3000)

            LoadResult.Page(
                data = items,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
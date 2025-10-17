package com.example.lessons.modules.backendApi.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.lessons.models.Rating
import com.example.lessons.modules.backendApi.ApiService

class TeacherRatingPagingSource(
    private val apiService: ApiService,
    private val id: String?
): PagingSource<Int, Rating>() {
    override fun getRefreshKey(state: PagingState<Int, Rating>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Rating> {
        val page = params.key ?: 1
        return try {
            val response = if (id == null) {
                apiService.getOpinionsAboutMe(
                    page = page,
                    pageCount = params.loadSize,
                )
            } else {
                apiService.getTeacherRatings(
                    id = id,
                    page = page,
                    pageCount = params.loadSize,
                )
            }

            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
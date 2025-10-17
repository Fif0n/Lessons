package com.example.lessons.modules.backendApi.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.lessons.modules.backendApi.ApiService
import com.example.lessons.modules.backendApi.data.dto.LessonRequestDto
import com.example.lessons.ui.formDatas.LessonRequestsFormData

class LessonRequestPagingSource(
    private val apiService: ApiService,
    private val formData: LessonRequestsFormData
): PagingSource<Int, LessonRequestDto>() {
    override fun getRefreshKey(state: PagingState<Int, LessonRequestDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LessonRequestDto> {
        val page = params.key ?: 1
        return try {
            val response = apiService.getLessonRequestsData(
                page = page,
                pageCount = params.loadSize,
                status = formData.status,
                id = formData.id
            )

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
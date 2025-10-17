package com.example.lessons.modules.backendApi.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.lessons.modules.backendApi.ApiService
import com.example.lessons.modules.backendApi.data.dto.TeacherGeneralDataDto
import com.example.lessons.ui.formDatas.FindTeacherFormData

class TeacherPagingSource(
    private val apiService: ApiService,
    private val formData: FindTeacherFormData,
): PagingSource<Int, TeacherGeneralDataDto>() {
    override fun getRefreshKey(state: PagingState<Int, TeacherGeneralDataDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TeacherGeneralDataDto> {
        val page = params.key ?: 1
        return try {
            val response = apiService.getTeachersGeneralData(
                page = page,
                pageCount = params.loadSize,
                subject = formData.subject.keys.toList(),
                schoolLevel = formData.schoolLevel.keys.toList(),
                lessonPlace = formData.lessonPlace.keys.toList(),
                moneyRate = formData.moneyRate,
                minLessonLength = formData.minLessonLength,
                maxLessonLength = formData.maxLessonLength,
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

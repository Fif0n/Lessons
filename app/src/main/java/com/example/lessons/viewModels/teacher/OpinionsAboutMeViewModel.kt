package com.example.lessons.viewModels.teacher

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.lessons.modules.backendApi.ApiService
import com.example.lessons.modules.backendApi.data.remote.TeacherRatingPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OpinionsAboutMeViewModel @Inject constructor(
    private val apiService: ApiService,
): ViewModel() {

    val ratingFlow = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { TeacherRatingPagingSource(apiService, null) }
    ).flow.cachedIn(viewModelScope)
}
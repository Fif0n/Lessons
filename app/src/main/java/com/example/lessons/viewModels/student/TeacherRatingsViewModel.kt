package com.example.lessons.viewModels.student

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
class TeacherRatingsViewModel @Inject constructor(
    private val apiService: ApiService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val id: String = checkNotNull(savedStateHandle["id"])

    val ratingFlow = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { TeacherRatingPagingSource(apiService, id) }
    ).flow.cachedIn(viewModelScope)
}
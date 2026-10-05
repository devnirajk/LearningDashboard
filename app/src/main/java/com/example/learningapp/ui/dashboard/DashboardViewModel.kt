package com.example.learningapp.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningapp.data.local.db.AppDatabase
import com.example.learningapp.data.repository.CourseRepositoryImpl
import com.example.learningapp.domain.model.Course
import com.example.learningapp.domain.repository.CourseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface DashboardUiState {
    object Loading : DashboardUiState
    data class Success(val courses: List<Course>) : DashboardUiState
    object Empty : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository: CourseRepository = CourseRepositoryImpl(
        courseDao = db.courseDao(),
        lessonDao = db.lessonDao()
    )

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        observeCourses()
        refresh()
    }

    private fun observeCourses() {
        repository.observeCourses()
            .onEach { courses ->
                _uiState.update {
                    if (courses.isEmpty()) DashboardUiState.Loading
                    else DashboardUiState.Success(courses)
                }
            }
            .catch { e ->
                _uiState.update { DashboardUiState.Error(e.message ?: "Unknown error") }
            }
            .launchIn(viewModelScope)
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                repository.refreshCourses()
            } catch (e: Exception) {
                // If refresh fails but we have cached data, stay in Success state
                if (_uiState.value is DashboardUiState.Loading) {
                    _uiState.update {
                        DashboardUiState.Error("Failed to load courses. Check your connection.")
                    }
                }
            }
        }
    }
}

package com.example.learningapp.ui.coursedetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.learningapp.data.local.db.AppDatabase
import com.example.learningapp.data.repository.CourseRepositoryImpl
import com.example.learningapp.domain.model.Course
import com.example.learningapp.domain.repository.CourseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Success(val course: Course) : DetailUiState
    data class Error(val message: String) : DetailUiState
}

class CourseDetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val courseId: Int = checkNotNull(savedStateHandle["courseId"])

    private val db = AppDatabase.getInstance(application)
    private val repository: CourseRepository = CourseRepositoryImpl(
        courseDao = db.courseDao(),
        lessonDao = db.lessonDao()
    )

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadCourse()
    }

    private fun loadCourse() {
        viewModelScope.launch {
            try {
                val course = repository.getCourseById(courseId)
                if (course != null) {
                    _uiState.update { DetailUiState.Success(course) }
                } else {
                    _uiState.update { DetailUiState.Error("Course not found") }
                }
            } catch (e: Exception) {
                _uiState.update { DetailUiState.Error(e.message ?: "Unknown error") }
            }
        }
    }

    fun markLessonCompleted(lessonId: Int) {
        viewModelScope.launch {
            repository.markLessonCompleted(courseId, lessonId)
            // Reload course to reflect new progress and lesson state
            loadCourse()
        }
    }
}

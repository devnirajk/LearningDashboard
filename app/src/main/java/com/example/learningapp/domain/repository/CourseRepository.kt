package com.example.learningapp.domain.repository

import com.example.learningapp.domain.model.Course
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    /** Emits cached courses from the local DB; triggers a network refresh. */
    fun observeCourses(): Flow<List<Course>>

    /** Returns a single course with its lessons from local DB. */
    suspend fun getCourseById(id: Int): Course?

    /** Marks a lesson as completed and recalculates course progress. */
    suspend fun markLessonCompleted(courseId: Int, lessonId: Int)

    /** Forces a fresh load from the remote source and caches locally. */
    suspend fun refreshCourses()
}

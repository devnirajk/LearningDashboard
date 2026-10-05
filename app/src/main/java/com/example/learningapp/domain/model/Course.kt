package com.example.learningapp.domain.model

data class Course(
    val id: Int,
    val title: String,
    val instructor: String,
    val progress: Int,          // 0–100
    val totalLessons: Int,
    val lessons: List<Lesson> = emptyList()
)

data class Lesson(
    val id: Int,
    val courseId: Int,
    val title: String,
    val isCompleted: Boolean
)

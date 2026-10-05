package com.example.learningapp.domain

import com.example.learningapp.domain.model.Lesson
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Tests the business logic for calculating course progress from lesson completion state.
 *
 * This mirrors the logic in CourseRepositoryImpl.markLessonCompleted and ensures
 * the formula (completedCount * 100 / totalLessons) is correct for key scenarios.
 */
class ProgressCalculationTest {

    private fun calculateProgress(lessons: List<Lesson>): Int {
        val total = lessons.size
        if (total == 0) return 0
        val completed = lessons.count { it.isCompleted }
        return (completed * 100) / total
    }

    @Test
    fun `progress is 0 when no lessons are completed`() {
        val lessons = listOf(
            Lesson(1, 1, "Intro", false),
            Lesson(2, 1, "Variables", false),
            Lesson(3, 1, "Functions", false)
        )
        assertEquals(0, calculateProgress(lessons))
    }

    @Test
    fun `progress is 100 when all lessons are completed`() {
        val lessons = listOf(
            Lesson(1, 1, "Intro", true),
            Lesson(2, 1, "Variables", true),
            Lesson(3, 1, "Functions", true)
        )
        assertEquals(100, calculateProgress(lessons))
    }

    @Test
    fun `progress is 50 when half lessons are completed`() {
        val lessons = listOf(
            Lesson(1, 1, "Intro", true),
            Lesson(2, 1, "Variables", true),
            Lesson(3, 1, "Functions", false),
            Lesson(4, 1, "OOP", false)
        )
        assertEquals(50, calculateProgress(lessons))
    }

    @Test
    fun `progress rounds down (integer division)`() {
        // 1 of 3 = 33.33% -> should be 33
        val lessons = listOf(
            Lesson(1, 1, "Intro", true),
            Lesson(2, 1, "Variables", false),
            Lesson(3, 1, "Functions", false)
        )
        assertEquals(33, calculateProgress(lessons))
    }

    @Test
    fun `progress is 0 for empty lesson list`() {
        assertEquals(0, calculateProgress(emptyList()))
    }

    @Test
    fun `marking a lesson completed updates progress correctly`() {
        // Simulate: 4 lessons, 1 already done; mark lesson 2 as done -> 2/4 = 50%
        val lessonsAfterMark = listOf(
            Lesson(1, 1, "Intro", true),
            Lesson(2, 1, "Variables", true),   // just marked
            Lesson(3, 1, "Functions", false),
            Lesson(4, 1, "OOP", false)
        )
        assertEquals(50, calculateProgress(lessonsAfterMark))
    }
}

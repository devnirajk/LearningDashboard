package com.example.learningapp.data.local.dao

import androidx.room.*
import com.example.learningapp.data.local.entity.LessonEntity

@Dao
interface LessonDao {

    @Query("SELECT * FROM lessons WHERE courseId = :courseId ORDER BY id ASC")
    suspend fun getByCourseId(courseId: Int): List<LessonEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(lessons: List<LessonEntity>)

    @Query("UPDATE lessons SET isCompleted = 1 WHERE id = :lessonId AND courseId = :courseId")
    suspend fun markCompleted(courseId: Int, lessonId: Int)

    @Query("SELECT COUNT(*) FROM lessons WHERE courseId = :courseId AND isCompleted = 1")
    suspend fun countCompleted(courseId: Int): Int

    @Query("SELECT COUNT(*) FROM lessons WHERE courseId = :courseId")
    suspend fun countTotal(courseId: Int): Int
}

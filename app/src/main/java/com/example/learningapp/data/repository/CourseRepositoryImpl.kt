package com.example.learningapp.data.repository

import com.example.learningapp.data.RemoteDataSource
import com.example.learningapp.data.local.dao.CourseDao
import com.example.learningapp.data.local.dao.LessonDao
import com.example.learningapp.data.local.entity.CourseEntity
import com.example.learningapp.data.local.entity.LessonEntity
import com.example.learningapp.domain.model.Course
import com.example.learningapp.domain.model.Lesson
import com.example.learningapp.domain.repository.CourseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CourseRepositoryImpl(
    private val courseDao: CourseDao,
    private val lessonDao: LessonDao,
    private val remoteDataSource: RemoteDataSource = RemoteDataSource
) : CourseRepository {

    override fun observeCourses(): Flow<List<Course>> =
        courseDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getCourseById(id: Int): Course? {
        val entity = courseDao.getById(id) ?: return null
        val lessons = lessonDao.getByCourseId(id).map { it.toDomain() }
        return entity.toDomain(lessons)
    }

    override suspend fun markLessonCompleted(courseId: Int, lessonId: Int) {
        lessonDao.markCompleted(courseId, lessonId)
        // Recalculate progress
        val completed = lessonDao.countCompleted(courseId)
        val total = lessonDao.countTotal(courseId)
        val newProgress = if (total > 0) (completed * 100) / total else 0
        courseDao.updateProgress(courseId, newProgress)
    }

    override suspend fun refreshCourses() {
        val remoteCourses = remoteDataSource.fetchCourses()
        val courseEntities = remoteCourses.map { it.toEntity() }
        val lessonEntities = remoteCourses.flatMap { course ->
            course.lessons.map { it.toEntity() }
        }
        courseDao.insertAll(courseEntities)
        lessonDao.insertAll(lessonEntities)
    }

    // ---- Mappers ----

    private fun CourseEntity.toDomain(lessons: List<Lesson> = emptyList()) = Course(
        id = id,
        title = title,
        instructor = instructor,
        progress = progress,
        totalLessons = totalLessons,
        lessons = lessons
    )

    private fun LessonEntity.toDomain() = Lesson(
        id = id,
        courseId = courseId,
        title = title,
        isCompleted = isCompleted
    )

    private fun Course.toEntity() = CourseEntity(
        id = id,
        title = title,
        instructor = instructor,
        progress = progress,
        totalLessons = totalLessons
    )

    private fun Lesson.toEntity() = LessonEntity(
        id = id,
        courseId = courseId,
        title = title,
        isCompleted = isCompleted
    )
}

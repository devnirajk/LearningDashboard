package com.example.learningapp.data

import com.example.learningapp.domain.model.Course
import com.example.learningapp.domain.model.Lesson
import kotlinx.coroutines.delay

/**
 * Simulates a remote API call with a short network delay.
 * In a real app this would be a Retrofit/Ktor service.
 */
object RemoteDataSource {

    @Throws(Exception::class)
    suspend fun fetchCourses(): List<Course> {
        delay(800) // Simulate network latency
        return listOf(
            Course(
                id = 1,
                title = "Python Programming",
                instructor = "John Smith",
                progress = 65,
                totalLessons = 20,
                lessons = listOf(
                    Lesson(101, 1, "Introduction", true),
                    Lesson(102, 1, "Variables & Data Types", true),
                    Lesson(103, 1, "Control Flow", true),
                    Lesson(104, 1, "Functions", true),
                    Lesson(105, 1, "Lists & Tuples", true),
                    Lesson(106, 1, "Dictionaries & Sets", true),
                    Lesson(107, 1, "File I/O", true),
                    Lesson(108, 1, "Exceptions", true),
                    Lesson(109, 1, "Modules", true),
                    Lesson(110, 1, "Classes & OOP", true),
                    Lesson(111, 1, "Inheritance", true),
                    Lesson(112, 1, "Iterators & Generators", true),
                    Lesson(113, 1, "Decorators", false),
                    Lesson(114, 1, "Context Managers", false),
                    Lesson(115, 1, "Regular Expressions", false),
                    Lesson(116, 1, "Threading & Concurrency", false),
                    Lesson(117, 1, "Unit Testing", false),
                    Lesson(118, 1, "Virtual Environments", false),
                    Lesson(119, 1, "Package Management", false),
                    Lesson(120, 1, "Project: CLI App", false)
                )
            ),
            Course(
                id = 2,
                title = "Generative AI",
                instructor = "Sarah Williams",
                progress = 40,
                totalLessons = 16,
                lessons = listOf(
                    Lesson(201, 2, "Introduction to AI", true),
                    Lesson(202, 2, "Machine Learning Basics", true),
                    Lesson(203, 2, "Neural Networks", true),
                    Lesson(204, 2, "Transformers Architecture", true),
                    Lesson(205, 2, "Large Language Models", true),
                    Lesson(206, 2, "Prompt Engineering", true),
                    Lesson(207, 2, "Fine-tuning Models", false),
                    Lesson(208, 2, "RAG Systems", false),
                    Lesson(209, 2, "Embeddings & Vector DBs", false),
                    Lesson(210, 2, "Image Generation", false),
                    Lesson(211, 2, "Multimodal Models", false),
                    Lesson(212, 2, "AI Safety & Ethics", false),
                    Lesson(213, 2, "Evaluation Metrics", false),
                    Lesson(214, 2, "Production Deployment", false),
                    Lesson(215, 2, "Monitoring AI Systems", false),
                    Lesson(216, 2, "Capstone Project", false)
                )
            ),
            Course(
                id = 3,
                title = "Full Stack Development",
                instructor = "David Brown",
                progress = 25,
                totalLessons = 28,
                lessons = listOf(
                    Lesson(301, 3, "HTML Fundamentals", true),
                    Lesson(302, 3, "CSS & Flexbox", true),
                    Lesson(303, 3, "JavaScript Basics", true),
                    Lesson(304, 3, "DOM Manipulation", true),
                    Lesson(305, 3, "Async JavaScript", true),
                    Lesson(306, 3, "React Basics", true),
                    Lesson(307, 3, "React Hooks", true),
                    Lesson(308, 3, "State Management", false),
                    Lesson(309, 3, "Routing", false),
                    Lesson(310, 3, "API Integration", false),
                    Lesson(311, 3, "Node.js Introduction", false),
                    Lesson(312, 3, "Express.js", false),
                    Lesson(313, 3, "REST API Design", false),
                    Lesson(314, 3, "Databases & SQL", false),
                    Lesson(315, 3, "NoSQL with MongoDB", false),
                    Lesson(316, 3, "Authentication & JWT", false),
                    Lesson(317, 3, "File Uploads", false),
                    Lesson(318, 3, "WebSockets", false),
                    Lesson(319, 3, "Testing", false),
                    Lesson(320, 3, "CI/CD", false),
                    Lesson(321, 3, "Docker", false),
                    Lesson(322, 3, "Cloud Deployment", false),
                    Lesson(323, 3, "Performance Optimization", false),
                    Lesson(324, 3, "Security Best Practices", false),
                    Lesson(325, 3, "TypeScript", false),
                    Lesson(326, 3, "GraphQL", false),
                    Lesson(327, 3, "Microservices", false),
                    Lesson(328, 3, "Capstone Project", false)
                )
            )
        )
    }
}

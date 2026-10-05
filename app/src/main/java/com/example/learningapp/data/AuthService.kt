package com.example.learningapp.data

import kotlinx.coroutines.delay

/**
 * Mock authentication service.
 * In production: call a real auth endpoint and return a JWT stored in EncryptedSharedPreferences.
 */
object AuthService {

    private val validCredentials = mapOf(
        "user@example.com" to "password123",
        "demo@learningapp.com" to "demo1234"
    )

    /**
     * Returns an auth token on success, throws AuthException on failure.
     */
    @Throws(AuthException::class)
    suspend fun login(email: String, password: String): String {
        delay(1000) // Simulate network
        val expected = validCredentials[email.lowercase().trim()]
        if (expected == null || expected != password) {
            throw AuthException("Invalid email or password")
        }
        return "mock_jwt_token_${System.currentTimeMillis()}"
    }

    class AuthException(message: String) : Exception(message)
}

package com.roomrental.android.data.model

data class User(
    val id: Long?,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String?,
    val userType: UserType,
    val createdAt: String?,
    val updatedAt: String?
)

enum class UserType {
    TENANT,
    LANDLORD,
    ADMIN
}

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String?,
    val userType: UserType
)

data class AuthResponse(
    val token: String,
    val type: String = "Bearer",
    val id: Long,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: String
) {
    fun toUser(): User {
        return User(
            id = id,
            username = username,
            email = email,
            firstName = firstName,
            lastName = lastName,
            phoneNumber = null,
            userType = when(role) {
                "TENANT" -> UserType.TENANT
                "LANDLORD" -> UserType.LANDLORD
                "ADMIN" -> UserType.ADMIN
                else -> UserType.TENANT
            },
            createdAt = null,
            updatedAt = null
        )
    }
}
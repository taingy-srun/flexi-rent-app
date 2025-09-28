package com.roomrental.android.data.repository

import com.roomrental.android.data.api.ApiClient
import com.roomrental.android.data.model.AuthResponse
import com.roomrental.android.data.model.LoginRequest
import com.roomrental.android.data.model.RegisterRequest
import com.roomrental.android.utils.PreferenceManager
import retrofit2.Response

class AuthRepository(private val preferenceManager: PreferenceManager) {
    private val apiService = ApiClient.getApiService()

    suspend fun login(username: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.login(LoginRequest(username, password))
            println("Login response code: ${response.code()}")
            println("Login response body: ${response.body()}")
            println("Login response raw: ${response.raw()}")

            if (response.isSuccessful) {
                val authResponse = response.body()
                if (authResponse != null) {
                    preferenceManager.saveAuthToken(authResponse.token)
                    preferenceManager.saveUser(authResponse.toUser())
                    Result.success(authResponse)
                } else {
                    Result.failure(Exception("Login successful but response body is null"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                println("Login error body: $errorBody")
                Result.failure(Exception("Login failed: ${response.code()} - ${response.message()} - $errorBody"))
            }
        } catch (e: Exception) {
            println("Login exception: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun register(registerRequest: RegisterRequest): Result<AuthResponse> {
        return try {
            val response = apiService.register(registerRequest)
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                preferenceManager.saveAuthToken(authResponse.token)
                preferenceManager.saveUser(authResponse.toUser())
                Result.success(authResponse)
            } else {
                Result.failure(Exception("Registration failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        preferenceManager.logout()
    }

    fun isLoggedIn(): Boolean {
        return preferenceManager.isLoggedIn()
    }

    fun getCurrentUser() = preferenceManager.getUser()
}
package com.submission.jktstoryapp.data

import com.submission.jktstoryapp.data.preference.UserPreference
import com.submission.jktstoryapp.data.response.LoginResponse
import com.submission.jktstoryapp.data.response.LoginResult
import com.submission.jktstoryapp.data.response.RegisterResponse
import com.submission.jktstoryapp.data.retrofit.ApiService

class AuthRepository(private val apiService: ApiService, private val userPreference: UserPreference)  {
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun saveSession(user: LoginResult) {
        userPreference.saveSession(user)
    }

    companion object {
        fun getInstance(apiService: ApiService, userPreference: UserPreference) = AuthRepository(apiService, userPreference)
    }
}
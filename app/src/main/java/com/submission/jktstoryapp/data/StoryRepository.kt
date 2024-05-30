package com.submission.jktstoryapp.data

import com.submission.jktstoryapp.data.preference.UserPreference
import com.submission.jktstoryapp.data.response.FileUploadResponse
import com.submission.jktstoryapp.data.response.ListStoryItem
import com.submission.jktstoryapp.data.response.LoginResponse
import com.submission.jktstoryapp.data.response.LoginResult
import com.submission.jktstoryapp.data.response.RegisterResponse
import com.submission.jktstoryapp.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
){





    fun getSession(): Flow<LoginResult> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        return userPreference.logout()
    }
    suspend fun getAllStory(): List<ListStoryItem?> {
        return apiService.getStories().listStory
    }
    suspend fun uploadStory(image: MultipartBody.Part, description: RequestBody): FileUploadResponse {
        return apiService.uploadStory(image, description)
    }

    companion object {
        fun getInstance(apiService: ApiService, userPreference: UserPreference) = StoryRepository(apiService, userPreference)
    }
}
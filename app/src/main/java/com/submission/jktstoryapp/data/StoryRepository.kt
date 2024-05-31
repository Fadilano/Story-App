package com.submission.jktstoryapp.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.submission.jktstoryapp.data.preference.UserPreference
import com.submission.jktstoryapp.data.response.FileUploadResponse
import com.submission.jktstoryapp.data.response.ListStoryItem
import com.submission.jktstoryapp.data.response.LoginResult
import com.submission.jktstoryapp.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    fun getSession(): Flow<LoginResult> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        return userPreference.logout()
    }

    fun getAllStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

    suspend fun getStoryWithLocation(location: Int = 1): List<ListStoryItem?> {
        return apiService.getStoriesWithLocation(location).listStory
    }

    suspend fun uploadStory(
        image: MultipartBody.Part,
        description: RequestBody
    ): FileUploadResponse {
        return apiService.uploadStory(image, description)
    }

    companion object {
        fun getInstance(apiService: ApiService, userPreference: UserPreference) =
            StoryRepository(apiService, userPreference)
    }
}
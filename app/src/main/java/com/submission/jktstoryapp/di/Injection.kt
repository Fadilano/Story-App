package com.submission.jktstoryapp.di

import android.content.Context
import com.submission.jktstoryapp.data.StoryRepository
import com.submission.jktstoryapp.data.preference.UserPreference
import com.submission.jktstoryapp.data.preference.dataStore
import com.submission.jktstoryapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token.toString())
        return StoryRepository.getInstance(apiService, pref)
    }
}
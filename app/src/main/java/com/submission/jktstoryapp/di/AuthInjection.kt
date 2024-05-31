package com.submission.jktstoryapp.di

import android.content.Context
import com.submission.jktstoryapp.data.AuthRepository
import com.submission.jktstoryapp.data.preference.UserPreference
import com.submission.jktstoryapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object AuthInjection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val userPreference = UserPreference.getInstance(context)
        val user = runBlocking { userPreference.getSession().first() }
        val authApiService = ApiConfig.getApiService(user.token.toString())
        return AuthRepository(authApiService, userPreference)
    }
}
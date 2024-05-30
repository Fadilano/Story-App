package com.submission.jktstoryapp.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.submission.jktstoryapp.data.response.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {


    suspend fun saveSession(user: LoginResult) {
        dataStore.edit { preferences ->
            preferences[NAME] = user.name ?: ""
            preferences[USER_ID] = user.userId ?: ""
            preferences[TOKEN_KEY] = user.token ?: ""
        }
    }

    fun getSession(): Flow<LoginResult> {
        return dataStore.data.map { preferences ->
            val name = preferences[NAME] ?: ""
            val userId = preferences[USER_ID] ?: ""
            val token = preferences[TOKEN_KEY] ?: ""
            LoginResult(name, userId, token)
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NAME = stringPreferencesKey("name")
        private val USER_ID = stringPreferencesKey("userId")
        private val TOKEN_KEY = stringPreferencesKey("token")

        fun getInstance(context: Context): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val dataStore = context.dataStore
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}
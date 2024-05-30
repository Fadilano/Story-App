package com.submission.jktstoryapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.submission.jktstoryapp.data.AuthRepository
import com.submission.jktstoryapp.data.StoryRepository
import com.submission.jktstoryapp.data.response.ErrorResponse
import com.submission.jktstoryapp.data.response.LoginResponse
import com.submission.jktstoryapp.data.response.LoginResult
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun login(email: String, password: String, onResult: (LoginResponse?, String?) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = authRepository.login(email, password)
                if (response.loginResult != null) {
                    authRepository.saveSession(response.loginResult)
                }
                onResult(response, null)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                onResult(null, errorResponse?.message)
            } finally {
                _isLoading.value = false
            }
        }
    }
    private suspend fun saveSession(user: LoginResult) {
        authRepository.saveSession(user)
    }



}
package com.submission.jktstoryapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.submission.jktstoryapp.data.AuthRepository
import com.submission.jktstoryapp.data.StoryRepository
import com.submission.jktstoryapp.data.response.ErrorResponse
import com.submission.jktstoryapp.data.response.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(
        name: String,
        email: String,
        password: String,
        onResult: (RegisterResponse?, String?) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = authRepository.register(name, email, password)
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
}
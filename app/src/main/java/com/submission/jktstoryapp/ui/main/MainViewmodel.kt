package com.submission.jktstoryapp.ui.main

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.submission.jktstoryapp.data.StoryRepository
import com.submission.jktstoryapp.data.response.ErrorResponse
import com.submission.jktstoryapp.data.response.ListStoryItem
import com.submission.jktstoryapp.data.response.LoginResult
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewmodel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _stories = MutableLiveData<List<ListStoryItem?>>()
    val stories: LiveData<List<ListStoryItem?>> = _stories

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllStories(context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val ListStory = storyRepository.getAllStory()
                _stories.value = ListStory
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                Toast.makeText(context, errorResponse.message, Toast.LENGTH_SHORT).show()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getSession(): LiveData<LoginResult?> {
        return storyRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }

    }

}
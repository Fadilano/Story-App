package com.submission.jktstoryapp.ui.map

import android.content.Context
import android.util.Log
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

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _stories = MutableLiveData<List<ListStoryItem?>>()
    val stories: LiveData<List<ListStoryItem?>> = _stories


    fun getStoryWithLocation(context: Context, location: Int = 1) {
        viewModelScope.launch {
            try {
                val ListStoryWithLocation = storyRepository.getStoryWithLocation(location)
                _stories.value = ListStoryWithLocation
                Log.d("MapsActivity", "Fetching stories with location: $location")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                Toast.makeText(context, errorResponse.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getSession(): LiveData<LoginResult?> {
        return storyRepository.getSession().asLiveData()
    }
}

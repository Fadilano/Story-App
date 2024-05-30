package com.submission.jktstoryapp.ui.addStory

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.submission.jktstoryapp.data.StoryRepository
import com.submission.jktstoryapp.data.response.FileUploadResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class addStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _isresponse = MutableLiveData<Boolean>()
    val isresponse: LiveData<Boolean> = _isresponse
    fun uploadStory(description: String, imageFile: File, context: Context) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val requestBody = description.toRequestBody("text/plain".toMediaType())
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                val multipartBody = MultipartBody.Part.createFormData(
                    "photo",
                    imageFile.name,
                    requestImageFile
                )

                val response = storyRepository.uploadStory(multipartBody, requestBody)
                if (!response.error) {
                    _isresponse.value = true
                } else {
                    _isresponse.value = false
                }


            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
                Toast.makeText(context, errorResponse.message, Toast.LENGTH_SHORT).show()
            } finally {
                _isLoading.value = false
            }
        }

    }


}
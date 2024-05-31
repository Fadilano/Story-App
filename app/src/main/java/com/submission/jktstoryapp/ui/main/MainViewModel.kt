package com.submission.jktstoryapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.submission.jktstoryapp.data.StoryRepository
import com.submission.jktstoryapp.data.response.ListStoryItem
import com.submission.jktstoryapp.data.response.LoginResult
import kotlinx.coroutines.launch

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    val stories: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getAllStory().cachedIn(viewModelScope)


    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun getSession(): LiveData<LoginResult?> {
        return storyRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }

    }

}
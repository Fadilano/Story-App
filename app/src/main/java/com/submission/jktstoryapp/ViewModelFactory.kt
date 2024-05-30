package com.submission.jktstoryapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.submission.jktstoryapp.data.StoryRepository
import com.submission.jktstoryapp.di.Injection
import com.submission.jktstoryapp.ui.addStory.addStoryViewModel
import com.submission.jktstoryapp.ui.login.LoginViewModel
import com.submission.jktstoryapp.ui.main.MainViewmodel
import com.submission.jktstoryapp.ui.register.RegisterViewModel

class ViewModelFactory(private val storyRepository: StoryRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewmodel::class.java) -> {
                MainViewmodel(storyRepository) as T
            }
            modelClass.isAssignableFrom(addStoryViewModel::class.java) -> {
                addStoryViewModel(storyRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(context: Context) = ViewModelFactory(Injection.provideRepository(context))
    }
}
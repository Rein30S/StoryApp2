package com.rickys.storyapp.ui.mainstory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rickys.storyapp.api.response.ListStoryItem
import com.rickys.storyapp.data.StoryRepository

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _toastText = MutableLiveData<String?>()

    val toastText: LiveData<String?> = _toastText

    fun getPagesStories(): LiveData<PagingData<ListStoryItem>> {
        return storyRepository.getPagesStories().cachedIn(viewModelScope)
    }

    fun logout() {
        storyRepository.logout()
    }
}
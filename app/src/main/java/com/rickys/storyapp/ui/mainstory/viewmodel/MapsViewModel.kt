package com.rickys.storyapp.ui.mainstory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rickys.storyapp.api.response.ListStoryItem
import com.rickys.storyapp.api.response.ListStoryResponse
import com.rickys.storyapp.data.StoryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _listLocationStory = MutableLiveData<List<ListStoryItem>>()

    val listLocationStory: LiveData<List<ListStoryItem>> = _listLocationStory

    init {
        getStoryWithLocation()
    }

    private fun getStoryWithLocation() {
        storyRepository.getStoryWithLocation()
            .enqueue(object : Callback<ListStoryResponse> {
                override fun onResponse(
                    call: Call<ListStoryResponse>,
                    response: Response<ListStoryResponse>
                ) {
                    val responseBody = response.body()
                    if (response.isSuccessful) {
                        _listLocationStory.value = responseBody?.listStory
                        Timber.d(responseBody?.listStory.toString())
                    } else {
                        Timber.e("Failed: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ListStoryResponse>, t: Throwable) {
                    Timber.e("Failed: ${t.message}")
                }

            })
    }
}
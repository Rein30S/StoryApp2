package com.rickys.storyapp.ui.mainstory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rickys.storyapp.api.response.RegisterResponse
import com.rickys.storyapp.data.StoryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _toastText = MutableLiveData<String>()
    private val _isSuccess = MutableLiveData<Boolean>()
    private val _isLoading = MutableLiveData<Boolean>()

    val toastText: LiveData<String> = _toastText
    val isSuccess: LiveData<Boolean> = _isSuccess
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(name: String, email: String, password: String) {
        _isLoading.value = true

        storyRepository.register(name, email, password)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.body()?.error == false) {
                        _toastText.value = "Register success"
                        _isSuccess.value = true
                        _isLoading.value = false
                    } else {
                        _toastText.value = "Register failed: ${response.message()}"
                        _isLoading.value = false
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    _toastText.value = "Register failed: ${t.message.toString()}"
                    _isLoading.value = false
                }
            })
    }
}
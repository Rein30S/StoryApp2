package com.rickys.storyapp.ui.mainstory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rickys.storyapp.api.response.LoginResponse
import com.rickys.storyapp.data.StoryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _toastText = MutableLiveData<String>()
    private val _isSuccess = MutableLiveData<Boolean>()
    private val _isLoading = MutableLiveData<Boolean>()

    val toastText: LiveData<String> = _toastText
    val isSuccess: LiveData<Boolean> = _isSuccess
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        _isLoading.value = true

        storyRepository.login(email, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    response.body()
                    if (response.body()?.error == false) {
                        val token = response.body()?.loginResult?.token
                        _toastText.value = "Login success"
                        _isLoading.value = false
                        _isSuccess.value = true

                        storeAuthorizationToken(token)
                    } else {
                        _isLoading.value = false
                        _toastText.value = "Login Failed: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _toastText.value = "Login Failed: ${t.message}"
                }

            })
    }

    private fun storeAuthorizationToken(token: String?) {
        token?.let {
            storyRepository.storeAuthorizationToken(it)
        }
    }
}
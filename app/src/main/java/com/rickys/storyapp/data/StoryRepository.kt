package com.rickys.storyapp.data

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.rickys.storyapp.api.response.AddStoryResponse
import com.rickys.storyapp.api.response.ListStoryItem
import com.rickys.storyapp.api.response.ListStoryResponse
import com.rickys.storyapp.api.response.LoginResponse
import com.rickys.storyapp.api.response.RegisterResponse
import com.rickys.storyapp.api.retrofit.ApiService
import com.rickys.storyapp.database.StoryDatabase
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

class StoryRepository(
    private val sharedPreferences: SharedPreferences,
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getPagesStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(getToken(), storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getPageStory()
            }
        ).liveData
    }

    fun getStoryWithLocation(): Call<ListStoryResponse> {
        return apiService.getStoriesWithLocation(getToken())
    }

    fun login(email: String, password: String): Call<LoginResponse> {
        return apiService.login(email, password)
    }

    fun register(name: String, email: String, password: String): Call<RegisterResponse> {
        return apiService.register(name, email, password)
    }

    fun addNewStory(
        multipart: MultipartBody.Part,
        descriptionBody: RequestBody,
        latBody: RequestBody?,
        lonBody: RequestBody?
    ): Call<AddStoryResponse> {
        return apiService.addNewStory(getToken(), multipart, descriptionBody, latBody, lonBody)
    }

    fun storeAuthorizationToken(token: String) {
        sharedPreferences.edit()
            .putString(TOKEN_KEY, token)
            .apply()
    }

    private fun getToken(): String {
        return "Bearer ${sharedPreferences.getString(TOKEN_KEY, "")}"
    }

    fun logout() {
        sharedPreferences.edit()
            .remove(TOKEN_KEY)
            .apply()
    }


    companion object {
        private const val TOKEN_KEY = "login_token"

        @Volatile
        private var instance: StoryRepository? = null

        @JvmStatic
        fun getInstance(
            sharedPreferences: SharedPreferences,
            storyDatabase: StoryDatabase,
            apiService: ApiService
        ): StoryRepository {
            return instance ?: synchronized(this) {
                StoryRepository(sharedPreferences, storyDatabase, apiService)
            }
        }
    }
}
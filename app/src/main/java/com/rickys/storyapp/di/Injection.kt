package com.rickys.storyapp.di


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.rickys.storyapp.data.StoryRepository
import com.rickys.storyapp.api.retrofit.ApiConfig
import com.rickys.storyapp.database.StoryDatabase


val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val sharedPreferences =
            context.getSharedPreferences("story_app_prefs", Context.MODE_PRIVATE)
        val apiService = ApiConfig.getApiService()
        val storyDatabase = StoryDatabase.getInstance(context)

        return StoryRepository.getInstance(sharedPreferences, storyDatabase, apiService)
    }
}
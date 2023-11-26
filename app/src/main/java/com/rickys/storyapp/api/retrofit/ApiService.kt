package com.rickys.storyapp.api.retrofit

import com.rickys.storyapp.api.response.AddStoryResponse
import com.rickys.storyapp.api.response.ListStoryResponse
import com.rickys.storyapp.api.response.LoginResponse
import com.rickys.storyapp.api.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") authToken: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 0
    ): ListStoryResponse

    @GET("stories")
    fun getStoriesWithLocation(
        @Header("Authorization") authToken: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 1
    ): Call<ListStoryResponse>

    @Multipart
    @POST("stories")
    fun addNewStory(
        @Header("Authorization") authToken: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null,
    ): Call<AddStoryResponse>
}
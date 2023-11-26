package com.rickys.storyapp.api.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

@Entity(tableName = "story")
@Parcelize
data class ListStoryItem(

	@PrimaryKey
	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("lat")
	val lat: Double? = null,

	@field:SerializedName("lon")
	val lon: Double? = null

) : Parcelable

data class ListStoryResponse (
	@field:SerializedName("listStory")
	val listStory: List<ListStoryItem>,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

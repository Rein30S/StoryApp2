package com.rickys.storyapp

import com.rickys.storyapp.api.response.ListStoryItem

object DataDummy {
    fun generateDummyStories() : List<ListStoryItem> {
        val newList = ArrayList<ListStoryItem>()
        for (i in 0..10) {
            val stories = ListStoryItem(
                photoUrl = "photo_url ",
                name = "Story $i",
                description = "Description $i",
                lon = 1.0,
                id = "id $i",
                lat = 2.0
            )
            newList.add(stories)
        }
        return newList
    }
}
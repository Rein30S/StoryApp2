package com.rickys.storyapp.ui.mainstory.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.rickys.storyapp.R
import com.rickys.storyapp.api.response.ListStoryItem
import com.rickys.storyapp.databinding.ActivityDetailStoryBinding
import com.rickys.storyapp.withDateFormat

class DetailStoryActivity : AppCompatActivity() {

    private var _binding: ActivityDetailStoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detail_story)

        val detailData = intent.getParcelableExtra<ListStoryItem>(STORY_EXTRA) as ListStoryItem

        binding.apply {
            tvDetailDescription.text = detailData.description
            tvDetailCreatedAt.text = detailData.createdAt?.withDateFormat()
            tvDetailCreatedBy.text = detailData.name
            Glide.with(binding.root.context!!)
                .load(detailData.photoUrl)
                .into(imgStory)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        const val STORY_EXTRA = "Story_Extra"
    }
}
package com.rickys.storyapp.ui.mainstory.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.rickys.storyapp.api.response.ListStoryItem
import com.rickys.storyapp.databinding.ItemStoryBinding
import com.rickys.storyapp.ui.mainstory.activity.DetailStoryActivity
import com.rickys.storyapp.withDateFormat

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        story?.let {
            holder.bind(story)
        }
    }

    inner class StoryViewHolder(private val itemBinding: ItemStoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(story: ListStoryItem) {
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .centerCrop()
                .into(itemBinding.imgStory)

            itemBinding.tvStoryUsername.text = story.name
            itemBinding.tvStoryDescription.text = story.description
            itemBinding.tvStoryCreatedAt.text = story.createdAt?.withDateFormat()

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.STORY_EXTRA, story)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(itemBinding.imgStory, "photo"),
                        Pair(itemBinding.tvStoryUsername, "name"),
                        Pair(itemBinding.tvStoryDescription, "description"),
                        Pair(itemBinding.tvStoryCreatedAt, "createdate")
                    )

                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        private const val STORY_EXTRA = "story"
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}
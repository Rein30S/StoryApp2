package com.rickys.storyapp.ui.mainstory.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.rickys.storyapp.R
import com.rickys.storyapp.ui.mainstory.viewmodel.ViewModelFactory
import com.rickys.storyapp.api.response.ListStoryItem
import com.rickys.storyapp.databinding.ActivityMainBinding
import com.rickys.storyapp.ui.mainstory.viewmodel.MainViewModel
import com.rickys.storyapp.ui.mainstory.adapter.StoryAdapter
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoryAdapter
    private val viewModel by viewModels<MainViewModel> { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = StoryAdapter()
        binding.rvStoryList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false).also {
                it.isSmoothScrollbarEnabled = true
                it.stackFromEnd = false
                observeViewModel()
            }

        playAnimation()

        binding.apply {
            btnAddStory.setOnClickListener {
                val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
                startActivity(intent)
            }
            binding.btnMaps.setOnClickListener {
                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume: ")
    }

    private fun observeViewModel() {
        viewModel.getPagesStories().observe(this) {
            loadStoryData(it)
        }

        viewModel.toastText.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }

    private fun loadStoryData(pagingData: PagingData<ListStoryItem>) {
        viewModel.getPagesStories().observe(this@MainActivity){
            adapter.submitData(lifecycle, pagingData)
        }
        binding.rvStoryList.adapter = adapter
        binding.rvStoryList.setHasFixedSize(true)
    }

    private fun playAnimation() {
        val rvStory = ObjectAnimator.ofFloat(binding.rvStoryList, View.ALPHA, 1f).setDuration(1000L)
        AnimatorSet().apply {
            playSequentially(rvStory)
            start()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_language -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
            }

            R.id.action_logout -> {
                viewModel.logout()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

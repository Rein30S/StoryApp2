package com.rickys.storyapp.ui.mainstory.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.rickys.storyapp.ui.mainstory.viewmodel.ViewModelFactory
import com.rickys.storyapp.databinding.ActivitySplashScreenBinding
import com.rickys.storyapp.ui.mainstory.viewmodel.MainViewModel

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = applicationContext.getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE)

        val token = getLoginToken()

        Handler(Looper.getMainLooper()).postDelayed({
            if (!token.isNullOrEmpty()) {
                Intent(this@SplashScreenActivity, MainActivity::class.java).also {
                    startActivity(it)
                }
            } else {
                Intent(this@SplashScreenActivity, LoginActivity::class.java).also {
                    startActivity(it)
                }
            }

            finish()

        }, 1200)
    }

    private fun getLoginToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    companion object {
        private const val SHARED_PREF_KEY = "story_app_prefs"
        private const val TOKEN_KEY = "login_token"
    }
}
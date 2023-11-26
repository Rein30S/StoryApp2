package com.rickys.storyapp.ui.mainstory.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.rickys.storyapp.R
import com.rickys.storyapp.ui.mainstory.viewmodel.ViewModelFactory
import com.rickys.storyapp.databinding.ActivityLoginBinding
import com.rickys.storyapp.ui.mainstory.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpView()
        setUpAction()
        playAnimation()
        showLoading(false)

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUpAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            when {
                email.isEmpty() -> {
                    binding.edLoginEmail.error = getString(R.string.EmptyEmailPassword)
                }

                password.isEmpty() -> {
                    binding.edLoginPassword.error = getString(R.string.EmptyEmailPassword)
                }

                email.isNotEmpty() && password.isNotEmpty() -> {
                    viewModel.login(email, password)
                    setUpViewModel()
                }
            }
        }
    }

    private fun setUpViewModel() {
        viewModel.toastText.observe(this) {
            Toast.makeText(this@LoginActivity, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.isSuccess.observe(this) {
            if (it) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        viewModel.isLoading.observe(this) {
            it?.let {
                binding.progressBar.isVisible = it
            }
        }
    }

    private fun setUpView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val tvEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(duration)
        val edLoginEmail = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(
            duration
        )
        val tvPassword = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(
            duration
        )
        val edLoginPassword =
            ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(
                duration
            )
        val btnLogin =
            ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(duration)
        val tvRegister = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(
            duration
        )

        val together = AnimatorSet().apply {
            playTogether(tvEmail, tvPassword)
        }

        AnimatorSet().apply {
            playSequentially(together, edLoginEmail, edLoginPassword, btnLogin, tvRegister)
            start()
        }
    }

    companion object {
        const val duration = 1000L
    }
}


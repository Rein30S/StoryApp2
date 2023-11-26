package com.rickys.storyapp.ui.mainstory.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.rickys.storyapp.R
import com.rickys.storyapp.ui.mainstory.viewmodel.ViewModelFactory
import com.rickys.storyapp.databinding.ActivityRegisterBinding
import com.rickys.storyapp.ui.mainstory.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        setUpView()
        setUpViewModel()
        setUpAction()
        showLoading(false)
    }

    private fun setUpAction(){
        binding.btnRegister.setOnClickListener{
            val name = binding.edRegName.text.toString()
            val email = binding.edRegEmail.text.toString()
            val password = binding.edRegPassword.text.toString()
            when{
                name.isEmpty() -> {
                    binding.edRegName.error = getString(R.string.EmptyName)
                }
                email.isEmpty() -> {
                    binding.edRegEmail.error = getString(R.string.EmptyEmail)
                }
                password.isEmpty() -> {
                    binding.edRegPassword.error = getString(R.string.EmptyPassword)
                }
                password.length < 8 -> {
                    binding.edRegPassword.error = getString(R.string.shortPassword)
                }
                else -> {
                    viewModel.register(name, email, password)
                    setUpViewModel()
                }
            }
        }
    }

    private fun setUpViewModel() {
        viewModel.toastText.observe(this) {
            Toast.makeText(this@RegisterActivity, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.isSuccess.observe(this) {
            if (it) {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
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

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

    private fun setUpView(){
        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

}
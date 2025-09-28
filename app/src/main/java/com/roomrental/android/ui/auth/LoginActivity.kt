package com.roomrental.android.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.roomrental.android.utils.FallbackDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.roomrental.android.R
import com.roomrental.android.data.api.ApiClient
import com.roomrental.android.data.repository.AuthRepository
import com.roomrental.android.databinding.ActivityLoginBinding
import com.roomrental.android.ui.MainActivity
import com.roomrental.android.utils.PreferenceManager
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ApiClient.initialize(this)
        authRepository = AuthRepository(PreferenceManager(this))

        // Check if already logged in
        if (authRepository.isLoggedIn()) {
            navigateToMain()
            return
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (validateInput(username, password)) {
                performLogin(username, password)
            }
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Test dialog - remove this after testing
        binding.btnLogin.setOnLongClickListener {
            FallbackDialog.showSuccess(
                this,
                "Test Dialog",
                "This is a test dialog to verify it's working correctly."
            )
            true
        }
    }

    private fun validateInput(username: String, password: String): Boolean {
        if (username.isEmpty()) {
            binding.tilUsername.error = "Username is required"
            return false
        }
        binding.tilUsername.error = null

        if (password.isEmpty()) {
            binding.tilPassword.error = "Password is required"
            return false
        }
        binding.tilPassword.error = null

        return true
    }

    private fun performLogin(username: String, password: String) {
        showLoading(true)

        lifecycleScope.launch {
            authRepository.login(username, password)
                .onSuccess {
                    showLoading(false)
                    navigateToMain()
                }
                .onFailure { error ->
                    showLoading(false)
                    println("LoginActivity error: ${error.message}")
                    error.printStackTrace()
                    FallbackDialog.showError(
                        this@LoginActivity,
                        "Login Failed",
                        error.message ?: "An unknown error occurred. Please try again."
                    )
                }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !show
        binding.btnRegister.isEnabled = !show
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
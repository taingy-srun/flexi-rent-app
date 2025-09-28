package com.roomrental.android.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.roomrental.android.utils.CustomDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.roomrental.android.R
import com.roomrental.android.data.api.ApiClient
import com.roomrental.android.data.model.RegisterRequest
import com.roomrental.android.data.model.UserType
import com.roomrental.android.data.repository.AuthRepository
import com.roomrental.android.databinding.ActivityRegisterBinding
import com.roomrental.android.ui.MainActivity
import com.roomrental.android.utils.PreferenceManager
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ApiClient.initialize(this)
        authRepository = AuthRepository(PreferenceManager(this))

        setupUserTypeDropdown()
        setupClickListeners()
    }

    private fun setupUserTypeDropdown() {
        val userTypes = arrayOf("TENANT", "LANDLORD")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, userTypes)
        binding.etUserType.setAdapter(adapter)
        binding.etUserType.setText(userTypes[0], false)
    }

    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            if (validateInput()) {
                performRegistration()
            }
        }

        binding.tvLogin.setOnClickListener {
            finish()
        }
    }

    private fun validateInput(): Boolean {
        val username = binding.etUsername.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        // Reset errors
        binding.tilUsername.error = null
        binding.tilEmail.error = null
        binding.tilFirstName.error = null
        binding.tilLastName.error = null
        binding.tilPassword.error = null
        binding.tilConfirmPassword.error = null

        if (username.isEmpty()) {
            binding.tilUsername.error = "Username is required"
            return false
        }

        if (email.isEmpty()) {
            binding.tilEmail.error = "Email is required"
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Please enter a valid email"
            return false
        }

        if (firstName.isEmpty()) {
            binding.tilFirstName.error = "First name is required"
            return false
        }

        if (lastName.isEmpty()) {
            binding.tilLastName.error = "Last name is required"
            return false
        }

        if (password.isEmpty()) {
            binding.tilPassword.error = "Password is required"
            return false
        }

        if (password.length < 6) {
            binding.tilPassword.error = "Password must be at least 6 characters"
            return false
        }

        if (password != confirmPassword) {
            binding.tilConfirmPassword.error = "Passwords do not match"
            return false
        }

        return true
    }

    private fun performRegistration() {
        showLoading(true)

        val registerRequest = RegisterRequest(
            username = binding.etUsername.text.toString().trim(),
            email = binding.etEmail.text.toString().trim(),
            password = binding.etPassword.text.toString(),
            firstName = binding.etFirstName.text.toString().trim(),
            lastName = binding.etLastName.text.toString().trim(),
            phoneNumber = binding.etPhoneNumber.text.toString().trim().takeIf { it.isNotEmpty() },
            userType = UserType.valueOf(binding.etUserType.text.toString())
        )

        lifecycleScope.launch {
            authRepository.register(registerRequest)
                .onSuccess {
                    showLoading(false)
                    CustomDialog.showSuccess(
                        this@RegisterActivity,
                        "Welcome to FlexiRent!",
                        "Your account has been created successfully."
                    ) {
                        navigateToMain()
                    }
                }
                .onFailure { error ->
                    showLoading(false)
                    CustomDialog.showError(
                        this@RegisterActivity,
                        "Registration Failed",
                        error.message ?: "An unknown error occurred. Please try again."
                    )
                }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnRegister.isEnabled = !show
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
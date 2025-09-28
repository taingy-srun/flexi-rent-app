package com.roomrental.android.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.roomrental.android.data.repository.AuthRepository
import com.roomrental.android.databinding.ActivityProfileBinding
import com.roomrental.android.ui.auth.LoginActivity
import com.roomrental.android.utils.FallbackDialog
import com.roomrental.android.utils.PreferenceManager

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authRepository = AuthRepository(PreferenceManager(this))

        setupToolbar()
        setupUserData()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupUserData() {
        val currentUser = authRepository.getCurrentUser()
        if (currentUser == null) {
            // User not logged in, redirect to login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        binding.apply {
            // Set user information
            tvUserName.text = "${currentUser.firstName} ${currentUser.lastName}"
            tvEmail.text = currentUser.email
            tvUsername.text = currentUser.username
            tvFirstName.text = currentUser.firstName
            tvLastName.text = currentUser.lastName
            chipRole.text = currentUser.userType.name
        }
    }

    private fun setupClickListeners() {
        binding.btnMyBookings.setOnClickListener {
            // TODO: Navigate to bookings activity
            Toast.makeText(this, "My Bookings - Coming Soon", Toast.LENGTH_SHORT).show()
        }

        binding.btnChangePassword.setOnClickListener {
            // TODO: Navigate to change password activity
            Toast.makeText(this, "Change Password - Coming Soon", Toast.LENGTH_SHORT).show()
        }

        binding.btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }
    }

    private fun showLogoutConfirmation() {
        FallbackDialog.showConfirmation(
            this,
            "Logout",
            "Are you sure you want to logout?",
            "Logout",
            "Cancel",
            positiveCallback = {
                performLogout()
            }
        )
    }

    private fun performLogout() {
        authRepository.logout()

        // Clear the back stack and navigate to login
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
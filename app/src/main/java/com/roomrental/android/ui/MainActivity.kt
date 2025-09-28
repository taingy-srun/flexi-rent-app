package com.roomrental.android.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.roomrental.android.R
import com.roomrental.android.data.api.ApiClient
import com.roomrental.android.data.repository.AuthRepository
import com.roomrental.android.data.repository.PropertyRepository
import com.roomrental.android.databinding.ActivityMainBinding
import com.roomrental.android.ui.auth.LoginActivity
import com.roomrental.android.ui.profile.ProfileActivity
import com.roomrental.android.ui.property.PropertyAdapter
import com.roomrental.android.ui.property.PropertyDetailActivity
import com.roomrental.android.utils.PreferenceManager
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var propertyAdapter: PropertyAdapter
    private lateinit var propertyRepository: PropertyRepository
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ApiClient.initialize(this)
        propertyRepository = PropertyRepository()
        authRepository = AuthRepository(PreferenceManager(this))

        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
        loadProperties()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupRecyclerView() {
        propertyAdapter = PropertyAdapter { property ->
            val intent = Intent(this, PropertyDetailActivity::class.java)
            intent.putExtra("property", property.getSafeProperty())
            startActivity(intent)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = propertyAdapter
        }
    }

    private fun setupClickListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            loadProperties()
        }

        binding.btnSearch.setOnClickListener {
            val city = binding.etSearchCity.text.toString().trim()
            if (city.isNotEmpty()) {
                searchProperties(city)
            } else {
                loadProperties()
            }
        }
    }

    private fun loadProperties() {
        showLoading(true)
        lifecycleScope.launch {
            propertyRepository.getAvailableProperties()
                .onSuccess { properties ->
                    showLoading(false)
                    propertyAdapter.submitList(properties)
                    showEmptyState(properties.isEmpty())
                }
                .onFailure { error ->
                    showLoading(false)
                    Toast.makeText(
                        this@MainActivity,
                        "Failed to load properties: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    showEmptyState(true)
                }
        }
    }

    private fun searchProperties(city: String) {
        showLoading(true)
        lifecycleScope.launch {
            propertyRepository.searchProperties(city = city)
                .onSuccess { searchResponse ->
                    showLoading(false)
                    propertyAdapter.submitList(searchResponse.content)
                    showEmptyState(searchResponse.content.isEmpty())
                }
                .onFailure { error ->
                    showLoading(false)
                    Toast.makeText(
                        this@MainActivity,
                        "Search failed: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    showEmptyState(true)
                }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.swipeRefresh.isRefreshing = false
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun showEmptyState(show: Boolean) {
        binding.tvEmptyState.visibility = if (show) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            R.id.action_logout -> {
                logout()
                true
            }
            R.id.action_my_bookings -> {
                // TODO: Navigate to bookings
                Toast.makeText(this, "My Bookings - Coming Soon", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        authRepository.logout()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
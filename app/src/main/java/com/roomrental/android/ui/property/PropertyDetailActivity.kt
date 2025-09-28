package com.roomrental.android.ui.property

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.roomrental.android.utils.FallbackDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.roomrental.android.R
import com.roomrental.android.data.api.ApiClient
import com.roomrental.android.data.model.BookingCreateRequest
import com.roomrental.android.data.model.Property
import com.roomrental.android.data.repository.AuthRepository
import com.roomrental.android.data.repository.BookingRepository
import com.roomrental.android.databinding.ActivityPropertyDetailBinding
import com.roomrental.android.utils.PreferenceManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PropertyDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPropertyDetailBinding
    private lateinit var property: Property
    private lateinit var bookingRepository: BookingRepository
    private lateinit var authRepository: AuthRepository

    private var startDate: Calendar? = null
    private var endDate: Calendar? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ApiClient.initialize(this)
        bookingRepository = BookingRepository()
        authRepository = AuthRepository(PreferenceManager(this))

        // Get property from intent
        property = intent.getParcelableExtra("property") ?: run {
            finish()
            return
        }

        setupToolbar()
        setupPropertyDetails()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.collapsingToolbarLayout.title = property.title
    }

    private fun setupPropertyDetails() {
        binding.apply {
            tvAddress.text = "${property.address}, ${property.city}, ${property.state} ${property.zipCode}"
            tvPrice.text = "$${property.pricePerMonth}/month"
            tvDescription.text = property.description ?: "No description available"

            chipBedrooms.text = "${property.bedrooms} Bedrooms"
            chipBathrooms.text = "${property.bathrooms} Bathrooms"
            chipArea.text = "${property.areaSqft} sqft"
            chipPropertyType.text = property.propertyType?.name?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "Unknown"

            // Load property image
            val safeImageUrls = property.getSafeImageUrls()
            if (safeImageUrls.isNotEmpty()) {
                Glide.with(this@PropertyDetailActivity)
                    .load(safeImageUrls.first())
                    .placeholder(R.drawable.ic_home)
                    .error(R.drawable.ic_home)
                    .into(ivPropertyImage)
            }

            // Setup amenities
            setupAmenities()

            // Disable booking if property is not available
            if (!property.available) {
                btnBookProperty.text = "Not Available"
                btnBookProperty.isEnabled = false
            }
        }
    }

    private fun setupAmenities() {
        binding.chipGroupAmenities.removeAllViews()
        val safeAmenities = property.getSafeAmenities()
        safeAmenities.forEach { amenity ->
            val chip = Chip(this)
            chip.text = amenity.name.lowercase().replace("_", " ").replaceFirstChar { it.uppercase() }
            chip.isClickable = false
            binding.chipGroupAmenities.addView(chip)
        }
    }

    private fun setupClickListeners() {
        binding.etStartDate.setOnClickListener {
            showDatePicker { date ->
                startDate = date
                binding.etStartDate.setText(dateFormat.format(date.time))
                validateDates()
            }
        }

        binding.etEndDate.setOnClickListener {
            showDatePicker { date ->
                endDate = date
                binding.etEndDate.setText(dateFormat.format(date.time))
                validateDates()
            }
        }

        binding.btnBookProperty.setOnClickListener {
            println("Book Property button clicked")
            if (validateBooking()) {
                println("Validation passed, creating booking...")
                createBooking()
            } else {
                println("Validation failed")
            }
        }
    }

    private fun showDatePicker(onDateSelected: (Calendar) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                onDateSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Don't allow past dates
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun validateDates() {
        if (startDate != null && endDate != null) {
            if (endDate!!.before(startDate)) {
                binding.tilEndDate.error = "End date must be after start date"
                binding.btnBookProperty.isEnabled = false
            } else {
                binding.tilEndDate.error = null
                binding.btnBookProperty.isEnabled = property.available
            }
        }
    }

    private fun validateBooking(): Boolean {
        println("Validating booking...")

        if (startDate == null) {
            println("Start date is null")
            binding.tilStartDate.error = "Please select start date"
            return false
        }

        if (endDate == null) {
            println("End date is null")
            binding.tilEndDate.error = "Please select end date"
            return false
        }

        if (endDate!!.before(startDate)) {
            println("End date is before start date")
            binding.tilEndDate.error = "End date must be after start date"
            return false
        }

        val currentUser = authRepository.getCurrentUser()
        if (currentUser == null) {
            println("Current user is null")
            FallbackDialog.showError(this, "Login Required", "Please login to make a booking")
            return false
        }

        println("Validation successful")
        return true
    }

    private fun createBooking() {
        val currentUser = authRepository.getCurrentUser() ?: return
        val preferenceManager = PreferenceManager(this)
        val token = preferenceManager.getAuthToken()

        println("Creating booking for user: ${currentUser.username} (ID: ${currentUser.id})")
        println("Property: ${property.title} (ID: ${property.id})")
        println("Dates: ${dateFormat.format(startDate!!.time)} to ${dateFormat.format(endDate!!.time)}")
        println("Auth token present: ${token != null}")
        println("Auth token length: ${token?.length ?: 0}")

        showLoading(true)

        println("Property ID: ${property.id}")
        println("Tenant ID: ${currentUser.id}")
        println("Start Date: ${dateFormat.format(startDate!!.time)}")
        println("End Date: ${dateFormat.format(endDate!!.time)}")

        val bookingRequest = BookingCreateRequest(
            propertyId = property.id!!,
            tenantId = currentUser.id!!,
            startDate = dateFormat.format(startDate!!.time),
            endDate = dateFormat.format(endDate!!.time)
        )

        println("Booking request: $bookingRequest")

        lifecycleScope.launch {
            // First check availability
            println("Checking property availability...")
            bookingRepository.checkPropertyAvailability(
                property.id!!,
                dateFormat.format(startDate!!.time),
                dateFormat.format(endDate!!.time)
            ).onSuccess { isAvailable ->
                println("Property availability check result: $isAvailable")
                if (!isAvailable) {
                    showLoading(false)
                    FallbackDialog.showError(
                        this@PropertyDetailActivity,
                        "Property Not Available",
                        "The property is not available for the selected dates. Please choose different dates."
                    )
                    return@onSuccess
                }

                // Proceed with booking if available
                println("Property is available, proceeding with booking...")
                bookingRepository.createBooking(bookingRequest)
                    .onSuccess { booking ->
                        showLoading(false)
                        println("Booking successful: $booking")
                        FallbackDialog.showSuccess(
                            this@PropertyDetailActivity,
                            "Booking Confirmed!",
                            "Your booking request has been submitted successfully. The landlord will review and respond soon."
                        ) {
                            finish()
                        }
                    }
                    .onFailure { error ->
                        showLoading(false)
                        println("Booking failed: ${error.message}")
                        error.printStackTrace()
                        FallbackDialog.showError(
                            this@PropertyDetailActivity,
                            "Booking Failed",
                            error.message ?: "Unable to submit booking request. Please try again."
                        )
                    }
            }.onFailure { error ->
                showLoading(false)
                println("Availability check failed: ${error.message}")
                FallbackDialog.showError(
                    this@PropertyDetailActivity,
                    "Availability Check Failed",
                    "Unable to check property availability. Please try again."
                )
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnBookProperty.isEnabled = !show
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
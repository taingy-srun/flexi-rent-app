package com.roomrental.android.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class Booking(
    val id: Long?,
    val propertyId: Long,
    val tenantId: Long,
    val landlordId: Long,
    val startDate: String,
    val endDate: String,
    val totalAmount: BigDecimal,
    val status: BookingStatus,
    val createdAt: String?,
    val updatedAt: String?
) : Parcelable

enum class BookingStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    REJECTED,
    COMPLETED
}

data class BookingCreateRequest(
    val propertyId: Long,
    val tenantId: Long,
    val startDate: String,
    val endDate: String
)

data class PropertyCreateRequest(
    val title: String,
    val description: String?,
    val address: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val country: String,
    val pricePerMonth: BigDecimal,
    val bedrooms: Int,
    val bathrooms: Int,
    val areaSqft: Int,
    val propertyType: PropertyType?,
    val landlordId: Long,
    val latitude: Double?,
    val longitude: Double?,
    val amenities: List<Amenity>? = emptyList(),
    val imageUrls: List<String>? = emptyList()
)
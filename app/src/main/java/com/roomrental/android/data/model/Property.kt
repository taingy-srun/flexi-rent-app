package com.roomrental.android.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.time.LocalDateTime

@Parcelize
data class Property(
    val id: Long?,
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
    val available: Boolean = true,
    val latitude: Double?,
    val longitude: Double?,
    val amenities: List<Amenity>?,
    val imageUrls: List<String>?,
    val createdAt: String?,
    val updatedAt: String?
) : Parcelable {

    // Helper method to get safe amenities list without nulls
    fun getSafeAmenities(): List<Amenity> {
        return amenities?.filterNotNull() ?: emptyList()
    }

    // Helper method to get safe image URLs list without nulls
    fun getSafeImageUrls(): List<String> {
        return imageUrls?.filterNotNull() ?: emptyList()
    }

    // Custom method to create a safe copy for Parcelable operations
    fun getSafeProperty(): Property {
        return this.copy(
            amenities = getSafeAmenities(),
            imageUrls = getSafeImageUrls()
        )
    }
}

enum class PropertyType {
    APARTMENT,
    HOUSE,
    CONDO,
    STUDIO,
    ROOM,
    DUPLEX,
    TOWNHOUSE
}

enum class Amenity {
    WIFI,
    PARKING,
    POOL,
    GYM,
    LAUNDRY,
    AIR_CONDITIONING,
    HEATING,
    DISHWASHER,
    PETS_ALLOWED,
    FURNISHED,
    BALCONY,
    GARDEN,
    ELEVATOR,
    SECURITY,
    STORAGE
}
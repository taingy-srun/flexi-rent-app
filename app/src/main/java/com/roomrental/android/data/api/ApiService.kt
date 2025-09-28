package com.roomrental.android.data.api

import com.roomrental.android.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Auth endpoints
    @POST("api/auth/signin")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>

    @POST("api/auth/signup")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<AuthResponse>

    // Property endpoints
    @GET("api/properties")
    suspend fun getAllProperties(): Response<List<Property>>

    @GET("api/properties/{id}")
    suspend fun getPropertyById(@Path("id") id: Long): Response<Property>

    @GET("api/properties/available")
    suspend fun getAvailableProperties(): Response<List<Property>>

    @GET("api/properties/search")
    suspend fun searchProperties(
        @Query("city") city: String? = null,
        @Query("minPrice") minPrice: String? = null,
        @Query("maxPrice") maxPrice: String? = null,
        @Query("bedrooms") bedrooms: Int? = null,
        @Query("propertyType") propertyType: PropertyType? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sortBy") sortBy: String = "createdAt",
        @Query("sortDir") sortDir: String = "desc"
    ): Response<PropertySearchResponse>

    @GET("api/properties/landlord/{landlordId}")
    suspend fun getPropertiesByLandlord(@Path("landlordId") landlordId: Long): Response<List<Property>>

    @POST("api/properties")
    suspend fun createProperty(@Body propertyRequest: PropertyCreateRequest): Response<Property>

    @PUT("api/properties/{id}")
    suspend fun updateProperty(
        @Path("id") id: Long,
        @Body propertyRequest: PropertyCreateRequest
    ): Response<Property>

    @DELETE("api/properties/{id}")
    suspend fun deleteProperty(@Path("id") id: Long): Response<Unit>

    // Booking endpoints
    @GET("api/bookings")
    suspend fun getAllBookings(): Response<List<Booking>>

    @GET("api/bookings/{id}")
    suspend fun getBookingById(@Path("id") id: Long): Response<Booking>

    @GET("api/bookings/tenant/{tenantId}")
    suspend fun getBookingsByTenant(@Path("tenantId") tenantId: Long): Response<List<Booking>>

    @GET("api/bookings/landlord/{landlordId}")
    suspend fun getBookingsByLandlord(@Path("landlordId") landlordId: Long): Response<List<Booking>>

    @GET("api/bookings/property/{propertyId}")
    suspend fun getBookingsByProperty(@Path("propertyId") propertyId: Long): Response<List<Booking>>

    @GET("api/bookings/property/{propertyId}/availability")
    suspend fun checkPropertyAvailability(
        @Path("propertyId") propertyId: Long,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<Boolean>

    @POST("api/bookings")
    suspend fun createBooking(@Body bookingRequest: BookingCreateRequest): Response<Booking>

    @PUT("api/bookings/{id}/status")
    suspend fun updateBookingStatus(
        @Path("id") id: Long,
        @Query("status") status: BookingStatus
    ): Response<Booking>

    @PUT("api/bookings/{id}/confirm")
    suspend fun confirmBooking(@Path("id") id: Long): Response<Booking>

    @PUT("api/bookings/{id}/cancel")
    suspend fun cancelBooking(@Path("id") id: Long): Response<Booking>

    @PUT("api/bookings/{id}/reject")
    suspend fun rejectBooking(@Path("id") id: Long): Response<Booking>
}

data class PropertySearchResponse(
    val content: List<Property>,
    val totalElements: Long,
    val totalPages: Int,
    val size: Int,
    val number: Int
)
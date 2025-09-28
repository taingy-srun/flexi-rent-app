package com.roomrental.android.data.repository

import com.roomrental.android.data.api.ApiClient
import com.roomrental.android.data.model.Booking
import com.roomrental.android.data.model.BookingCreateRequest
import com.roomrental.android.data.model.BookingStatus

class BookingRepository {
    private val apiService = ApiClient.getApiService()

    suspend fun getAllBookings(): Result<List<Booking>> {
        return try {
            val response = apiService.getAllBookings()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch bookings: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBookingById(id: Long): Result<Booking> {
        return try {
            val response = apiService.getBookingById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Booking not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBookingsByTenant(tenantId: Long): Result<List<Booking>> {
        return try {
            val response = apiService.getBookingsByTenant(tenantId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch tenant bookings: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBookingsByLandlord(landlordId: Long): Result<List<Booking>> {
        return try {
            val response = apiService.getBookingsByLandlord(landlordId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch landlord bookings: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBookingsByProperty(propertyId: Long): Result<List<Booking>> {
        return try {
            val response = apiService.getBookingsByProperty(propertyId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch property bookings: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun checkPropertyAvailability(
        propertyId: Long,
        startDate: String,
        endDate: String
    ): Result<Boolean> {
        return try {
            val response = apiService.checkPropertyAvailability(propertyId, startDate, endDate)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to check availability: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createBooking(bookingRequest: BookingCreateRequest): Result<Booking> {
        return try {
            println("BookingRepository: Creating booking with request: $bookingRequest")
            val response = apiService.createBooking(bookingRequest)
            println("BookingRepository: Response code: ${response.code()}")
            println("BookingRepository: Response success: ${response.isSuccessful}")
            println("BookingRepository: Response message: ${response.message()}")

            if (response.isSuccessful && response.body() != null) {
                println("BookingRepository: Booking created successfully: ${response.body()}")
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                println("BookingRepository: Error body: $errorBody")
                Result.failure(Exception("Failed to create booking: ${response.message()} - $errorBody"))
            }
        } catch (e: Exception) {
            println("BookingRepository: Exception occurred: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun updateBookingStatus(id: Long, status: BookingStatus): Result<Booking> {
        return try {
            val response = apiService.updateBookingStatus(id, status)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to update booking status: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun confirmBooking(id: Long): Result<Booking> {
        return try {
            val response = apiService.confirmBooking(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to confirm booking: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun cancelBooking(id: Long): Result<Booking> {
        return try {
            val response = apiService.cancelBooking(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to cancel booking: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun rejectBooking(id: Long): Result<Booking> {
        return try {
            val response = apiService.rejectBooking(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to reject booking: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
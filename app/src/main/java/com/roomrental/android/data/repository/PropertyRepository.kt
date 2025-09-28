package com.roomrental.android.data.repository

import com.roomrental.android.data.api.ApiClient
import com.roomrental.android.data.api.PropertySearchResponse
import com.roomrental.android.data.model.Property
import com.roomrental.android.data.model.PropertyCreateRequest
import com.roomrental.android.data.model.PropertyType
import retrofit2.Response

class PropertyRepository {
    private val apiService = ApiClient.getApiService()

    suspend fun getAllProperties(): Result<List<Property>> {
        return try {
            val response = apiService.getAllProperties()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch properties: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAvailableProperties(): Result<List<Property>> {
        return try {
            val response = apiService.getAvailableProperties()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch available properties: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPropertyById(id: Long): Result<Property> {
        return try {
            val response = apiService.getPropertyById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Property not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchProperties(
        city: String? = null,
        minPrice: String? = null,
        maxPrice: String? = null,
        bedrooms: Int? = null,
        propertyType: PropertyType? = null,
        page: Int = 0,
        size: Int = 10
    ): Result<PropertySearchResponse> {
        return try {
            val response = apiService.searchProperties(
                city, minPrice, maxPrice, bedrooms, propertyType, page, size
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Search failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPropertiesByLandlord(landlordId: Long): Result<List<Property>> {
        return try {
            val response = apiService.getPropertiesByLandlord(landlordId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch landlord properties: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createProperty(propertyRequest: PropertyCreateRequest): Result<Property> {
        return try {
            val response = apiService.createProperty(propertyRequest)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to create property: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProperty(id: Long, propertyRequest: PropertyCreateRequest): Result<Property> {
        return try {
            val response = apiService.updateProperty(id, propertyRequest)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to update property: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProperty(id: Long): Result<Unit> {
        return try {
            val response = apiService.deleteProperty(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete property: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
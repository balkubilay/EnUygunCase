package com.example.enuyguncase.data.repository

import com.example.enuyguncase.data.api.ProductApiService
import com.example.enuyguncase.data.model.Product
import com.example.enuyguncase.data.model.ProductResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository(private val apiService: ProductApiService) {
    
    suspend fun getProducts(limit: Int = 30, skip: Int = 0): Result<ProductResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getProducts(limit, skip)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun getProduct(id: Int): Result<Product> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getProduct(id)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun searchProducts(query: String): Result<ProductResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.searchProducts(query)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun getProductsByCategory(category: String): Result<ProductResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getProductsByCategory(category)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
} 
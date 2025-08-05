package com.example.enuyguncase.data.api

import com.example.enuyguncase.data.model.Product
import com.example.enuyguncase.data.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {
    
    @GET("products")
    suspend fun getProducts(@Query("limit") limit: Int = 30, @Query("skip") skip: Int = 0): ProductResponse
    
    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Product
    
    @GET("products/search")
    suspend fun searchProducts(@Query("q") query: String): ProductResponse
    
    @GET("products/categories")
    suspend fun getCategories(): List<String>
    
    @GET("products")
    suspend fun getProductsByCategory(@Query("category") category: String): ProductResponse
} 
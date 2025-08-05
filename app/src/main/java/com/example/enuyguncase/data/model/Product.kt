package com.example.enuyguncase.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    @SerializedName("discountPercentage")
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val brand: String?,
    val category: String,
    val thumbnail: String,
    val images: List<String>
) 
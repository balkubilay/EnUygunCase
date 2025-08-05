package com.example.enuyguncase.data.model

data class FavoriteItem(
    val id: Int,
    val product: Product,
    val addedAt: Long = System.currentTimeMillis()
) 
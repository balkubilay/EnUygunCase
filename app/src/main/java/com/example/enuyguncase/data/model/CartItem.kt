package com.example.enuyguncase.data.model

data class CartItem(
    val id: Int,
    val product: Product,
    val quantity: Int,
    val addedAt: Long = System.currentTimeMillis()
) {
    val totalPrice: Double
        get() = product.price * quantity * (1 - product.discountPercentage / 100)
    
    val totalOriginalPrice: Double
        get() = product.price * quantity
} 
package com.example.enuyguncase.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.enuyguncase.data.model.CartItem
import com.example.enuyguncase.data.model.Product

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productId: Int,
    val productTitle: String,
    val productDescription: String,
    val productPrice: Double,
    val productDiscountPercentage: Double,
    val productRating: Double,
    val productStock: Int,
    val productBrand: String?,
    val productCategory: String,
    val productThumbnail: String,
    val productImages: String, // JSON array as string
    val quantity: Int,
    val addedAt: Long = System.currentTimeMillis()
) {
    fun toCartItem(): CartItem {
        val product = Product(
            id = productId,
            title = productTitle,
            description = productDescription,
            price = productPrice,
            discountPercentage = productDiscountPercentage,
            rating = productRating,
            stock = productStock,
            brand = productBrand ?: "",
            category = productCategory,
            thumbnail = productThumbnail,
            images = emptyList() // We'll handle images separately if needed
        )
        return CartItem(
            id = id,
            product = product,
            quantity = quantity,
            addedAt = addedAt
        )
    }
    
    companion object {
        fun fromCartItem(cartItem: CartItem): CartItemEntity {
            return CartItemEntity(
                id = cartItem.id,
                productId = cartItem.product.id,
                productTitle = cartItem.product.title,
                productDescription = cartItem.product.description,
                productPrice = cartItem.product.price,
                productDiscountPercentage = cartItem.product.discountPercentage,
                productRating = cartItem.product.rating,
                productStock = cartItem.product.stock,
                productBrand = cartItem.product.brand,
                productCategory = cartItem.product.category,
                productThumbnail = cartItem.product.thumbnail,
                productImages = "", // We'll handle images separately if needed
                quantity = cartItem.quantity,
                addedAt = cartItem.addedAt
            )
        }
    }
} 
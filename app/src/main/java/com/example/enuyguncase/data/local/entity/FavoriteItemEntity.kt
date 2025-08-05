package com.example.enuyguncase.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.enuyguncase.data.model.FavoriteItem
import com.example.enuyguncase.data.model.Product

@Entity(tableName = "favorite_items")
data class FavoriteItemEntity(
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
    val addedAt: Long = System.currentTimeMillis()
) {
    fun toFavoriteItem(): FavoriteItem {
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
        return FavoriteItem(
            id = id,
            product = product,
            addedAt = addedAt
        )
    }
    
    companion object {
        fun fromFavoriteItem(favoriteItem: FavoriteItem): FavoriteItemEntity {
            return FavoriteItemEntity(
                id = favoriteItem.id,
                productId = favoriteItem.product.id,
                productTitle = favoriteItem.product.title,
                productDescription = favoriteItem.product.description,
                productPrice = favoriteItem.product.price,
                productDiscountPercentage = favoriteItem.product.discountPercentage,
                productRating = favoriteItem.product.rating,
                productStock = favoriteItem.product.stock,
                productBrand = favoriteItem.product.brand,
                productCategory = favoriteItem.product.category,
                productThumbnail = favoriteItem.product.thumbnail,
                productImages = "", // We'll handle images separately if needed
                addedAt = favoriteItem.addedAt
            )
        }
    }
} 
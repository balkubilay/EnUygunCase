package com.example.enuyguncase.data.local.dao

import androidx.room.*
import com.example.enuyguncase.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    
    @Query("SELECT * FROM cart_items ORDER BY addedAt DESC")
    fun getAllCartItems(): Flow<List<CartItemEntity>>
    
    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    suspend fun getCartItemByProductId(productId: Int): CartItemEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItemEntity)
    
    @Update
    suspend fun updateCartItem(cartItem: CartItemEntity)
    
    @Delete
    suspend fun deleteCartItem(cartItem: CartItemEntity)
    
    @Query("DELETE FROM cart_items WHERE id = :cartItemId")
    suspend fun deleteCartItemById(cartItemId: Int)
    
    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteCartItemByProductId(productId: Int)
    
    @Query("DELETE FROM cart_items")
    suspend fun deleteAllCartItems()
    
    @Query("SELECT COUNT(*) FROM cart_items")
    suspend fun getCartItemCount(): Int
    
    @Query("SELECT SUM(quantity) FROM cart_items")
    suspend fun getTotalQuantity(): Int?
    
    @Query("SELECT SUM(quantity * productPrice * (1 - productDiscountPercentage / 100)) FROM cart_items")
    suspend fun getTotalPrice(): Double?
} 
package com.example.enuyguncase.data.local

import com.example.enuyguncase.data.local.dao.CartDao
import com.example.enuyguncase.data.local.dao.FavoriteDao
import com.example.enuyguncase.data.local.entity.CartItemEntity
import com.example.enuyguncase.data.local.entity.FavoriteItemEntity
import com.example.enuyguncase.data.model.CartItem
import com.example.enuyguncase.data.model.FavoriteItem
import com.example.enuyguncase.data.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseManager @Inject constructor(
    private val cartDao: CartDao,
    private val favoriteDao: FavoriteDao
) {
    
    // Cart Operations
    fun getAllCartItems(): Flow<List<CartItem>> {
        return cartDao.getAllCartItems().map { entities ->
            entities.map { it.toCartItem() }
        }
    }
    
    suspend fun getCartItemByProductId(productId: Int): CartItem? {
        return cartDao.getCartItemByProductId(productId)?.toCartItem()
    }
    
    suspend fun insertCartItem(cartItem: CartItem) {
        cartDao.insertCartItem(CartItemEntity.fromCartItem(cartItem))
    }
    
    suspend fun updateCartItem(cartItem: CartItem) {
        cartDao.updateCartItem(CartItemEntity.fromCartItem(cartItem))
    }
    
    suspend fun deleteCartItem(cartItem: CartItem) {
        cartDao.deleteCartItem(CartItemEntity.fromCartItem(cartItem))
    }
    
    suspend fun deleteCartItemById(cartItemId: Int) {
        cartDao.deleteCartItemById(cartItemId)
    }
    
    suspend fun deleteCartItemByProductId(productId: Int) {
        cartDao.deleteCartItemByProductId(productId)
    }
    
    suspend fun deleteAllCartItems() {
        cartDao.deleteAllCartItems()
    }
    
    suspend fun getCartItemCount(): Int {
        return cartDao.getCartItemCount()
    }
    
    suspend fun getTotalQuantity(): Int {
        return cartDao.getTotalQuantity() ?: 0
    }
    
    suspend fun getTotalPrice(): Double {
        return cartDao.getTotalPrice() ?: 0.0
    }
    
    // Favorite Operations
    fun getAllFavoriteItems(): Flow<List<FavoriteItem>> {
        return favoriteDao.getAllFavoriteItems().map { entities ->
            entities.map { it.toFavoriteItem() }
        }
    }
    
    suspend fun getFavoriteItemByProductId(productId: Int): FavoriteItem? {
        return favoriteDao.getFavoriteItemByProductId(productId)?.toFavoriteItem()
    }
    
    suspend fun insertFavoriteItem(favoriteItem: FavoriteItem) {
        favoriteDao.insertFavoriteItem(FavoriteItemEntity.fromFavoriteItem(favoriteItem))
    }
    
    suspend fun updateFavoriteItem(favoriteItem: FavoriteItem) {
        favoriteDao.updateFavoriteItem(FavoriteItemEntity.fromFavoriteItem(favoriteItem))
    }
    
    suspend fun deleteFavoriteItem(favoriteItem: FavoriteItem) {
        favoriteDao.deleteFavoriteItem(FavoriteItemEntity.fromFavoriteItem(favoriteItem))
    }
    
    suspend fun deleteFavoriteItemById(favoriteItemId: Int) {
        favoriteDao.deleteFavoriteItemById(favoriteItemId)
    }
    
    suspend fun deleteFavoriteItemByProductId(productId: Int) {
        favoriteDao.deleteFavoriteItemByProductId(productId)
    }
    
    suspend fun deleteAllFavoriteItems() {
        favoriteDao.deleteAllFavoriteItems()
    }
    
    suspend fun getFavoriteItemCount(): Int {
        return favoriteDao.getFavoriteItemCount()
    }
    
    suspend fun isProductInFavorites(productId: Int): Boolean {
        return favoriteDao.isProductInFavorites(productId)
    }
} 
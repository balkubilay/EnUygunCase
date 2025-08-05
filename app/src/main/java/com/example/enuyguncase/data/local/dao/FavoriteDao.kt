package com.example.enuyguncase.data.local.dao

import androidx.room.*
import com.example.enuyguncase.data.local.entity.FavoriteItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    
    @Query("SELECT * FROM favorite_items ORDER BY addedAt DESC")
    fun getAllFavoriteItems(): Flow<List<FavoriteItemEntity>>
    
    @Query("SELECT * FROM favorite_items WHERE productId = :productId LIMIT 1")
    suspend fun getFavoriteItemByProductId(productId: Int): FavoriteItemEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteItem(favoriteItem: FavoriteItemEntity)
    
    @Update
    suspend fun updateFavoriteItem(favoriteItem: FavoriteItemEntity)
    
    @Delete
    suspend fun deleteFavoriteItem(favoriteItem: FavoriteItemEntity)
    
    @Query("DELETE FROM favorite_items WHERE id = :favoriteItemId")
    suspend fun deleteFavoriteItemById(favoriteItemId: Int)
    
    @Query("DELETE FROM favorite_items WHERE productId = :productId")
    suspend fun deleteFavoriteItemByProductId(productId: Int)
    
    @Query("DELETE FROM favorite_items")
    suspend fun deleteAllFavoriteItems()
    
    @Query("SELECT COUNT(*) FROM favorite_items")
    suspend fun getFavoriteItemCount(): Int
    
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_items WHERE productId = :productId)")
    suspend fun isProductInFavorites(productId: Int): Boolean
} 
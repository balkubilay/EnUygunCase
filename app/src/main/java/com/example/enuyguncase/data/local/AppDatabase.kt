package com.example.enuyguncase.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.enuyguncase.data.local.dao.CartDao
import com.example.enuyguncase.data.local.dao.FavoriteDao
import com.example.enuyguncase.data.local.entity.CartItemEntity
import com.example.enuyguncase.data.local.entity.FavoriteItemEntity

@Database(
    entities = [
        CartItemEntity::class,
        FavoriteItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun cartDao(): CartDao
    abstract fun favoriteDao(): FavoriteDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 
package com.example.enuyguncase.data.repository

import com.example.enuyguncase.data.local.DatabaseManager
import com.example.enuyguncase.data.model.FavoriteItem
import com.example.enuyguncase.data.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(
    private val databaseManager: DatabaseManager
) {
    
    private val _favoriteItems = MutableStateFlow<List<FavoriteItem>>(emptyList())
    val favoriteItems: StateFlow<List<FavoriteItem>> = _favoriteItems.asStateFlow()
    
    private val _totalFavorites = MutableStateFlow(0)
    val totalFavorites: StateFlow<Int> = _totalFavorites.asStateFlow()
    
    private var nextId = 1
    private val scope = CoroutineScope(Dispatchers.IO)
    
    init {
        // Load saved favorite items when repository is initialized
        databaseManager.getAllFavoriteItems()
            .onEach { savedFavoriteItems ->
                _favoriteItems.value = savedFavoriteItems
                if (savedFavoriteItems.isNotEmpty()) {
                    nextId = (savedFavoriteItems.maxOfOrNull { it.id } ?: 0) + 1
                }
                updateTotal()
            }
            .launchIn(scope)
    }
    
    fun addToFavorites(product: Product) {
        if (!isInFavorites(product.id)) {
            val newItem = FavoriteItem(
                id = nextId++,
                product = product
            )
            _favoriteItems.value = _favoriteItems.value + newItem
            updateTotal()
            saveFavoriteItems()
        }
    }
    
    fun removeFromFavorites(productId: Int) {
        _favoriteItems.value = _favoriteItems.value.filter { it.product.id != productId }
        updateTotal()
        saveFavoriteItems()
    }
    
    fun removeFromFavoritesByItemId(itemId: Int) {
        _favoriteItems.value = _favoriteItems.value.filter { it.id != itemId }
        updateTotal()
        saveFavoriteItems()
    }
    
    fun clearFavorites() {
        _favoriteItems.value = emptyList()
        updateTotal()
        saveFavoriteItems()
    }
    
    fun isInFavorites(productId: Int): Boolean {
        return _favoriteItems.value.any { it.product.id == productId }
    }
    
    fun getFavoriteProducts(): List<Product> {
        return _favoriteItems.value.map { it.product }
    }
    
    private fun updateTotal() {
        _totalFavorites.value = _favoriteItems.value.size
    }
    
    private fun saveFavoriteItems() {
        scope.launch {
            // Clear existing items and insert new ones
            databaseManager.deleteAllFavoriteItems()
            _favoriteItems.value.forEach { favoriteItem ->
                databaseManager.insertFavoriteItem(favoriteItem)
            }
        }
    }
} 
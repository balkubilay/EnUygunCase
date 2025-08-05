package com.example.enuyguncase.data.repository

import com.example.enuyguncase.data.local.DatabaseManager
import com.example.enuyguncase.data.model.CartItem
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
class CartRepository @Inject constructor(
    private val databaseManager: DatabaseManager
) {
    
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()
    
    private val _totalItems = MutableStateFlow(0)
    val totalItems: StateFlow<Int> = _totalItems.asStateFlow()
    
    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()
    
    private var nextId = 1
    private val scope = CoroutineScope(Dispatchers.IO)
    
    init {
        // Load saved cart items when repository is initialized
        databaseManager.getAllCartItems()
            .onEach { savedCartItems ->
                _cartItems.value = savedCartItems
                if (savedCartItems.isNotEmpty()) {
                    nextId = (savedCartItems.maxOfOrNull { it.id } ?: 0) + 1
                }
                updateTotals()
            }
            .launchIn(scope)
    }
    
    fun addToCart(product: Product, quantity: Int = 1) {
        val existingItem = _cartItems.value.find { it.product.id == product.id }
        
        if (existingItem != null) {
            // Update existing item quantity
            val updatedItems = _cartItems.value.map { item ->
                if (item.product.id == product.id) {
                    item.copy(quantity = item.quantity + quantity)
                } else {
                    item
                }
            }
            _cartItems.value = updatedItems
        } else {
            // Add new item
            val newItem = CartItem(
                id = nextId++,
                product = product,
                quantity = quantity
            )
            _cartItems.value = _cartItems.value + newItem
        }
        
        updateTotals()
        saveCartItems()
    }
    
    fun removeFromCart(cartItemId: Int) {
        _cartItems.value = _cartItems.value.filter { it.id != cartItemId }
        updateTotals()
        saveCartItems()
    }
    
    fun updateQuantity(cartItemId: Int, newQuantity: Int) {
        android.util.Log.d("CartRepository", "updateQuantity called with cartItemId: $cartItemId, newQuantity: $newQuantity")
        android.util.Log.d("CartRepository", "Current cart items: ${_cartItems.value.map { "id=${it.id}, product=${it.product.title}, quantity=${it.quantity}" }}")
        
        if (newQuantity <= 0) {
            android.util.Log.d("CartRepository", "Removing item with id: $cartItemId")
            removeFromCart(cartItemId)
            return
        }
        
        val updatedItems = _cartItems.value.map { item ->
            if (item.id == cartItemId) {
                android.util.Log.d("CartRepository", "Updating item ${item.product.title} from quantity ${item.quantity} to $newQuantity")
                item.copy(quantity = newQuantity)
            } else {
                item
            }
        }
        _cartItems.value = updatedItems
        updateTotals()
        saveCartItems()
    }
    
    fun clearCart() {
        _cartItems.value = emptyList()
        updateTotals()
        saveCartItems()
    }
    
    fun isInCart(productId: Int): Boolean {
        return _cartItems.value.any { it.product.id == productId }
    }
    
    fun getCartItemQuantity(productId: Int): Int {
        return _cartItems.value.find { it.product.id == productId }?.quantity ?: 0
    }
    
    fun getCartItem(productId: Int): CartItem? {
        return _cartItems.value.find { it.product.id == productId }
    }
    
    private fun updateTotals() {
        val items = _cartItems.value
        _totalItems.value = items.sumOf { it.quantity }
        _totalPrice.value = items.sumOf { it.totalPrice }
    }
    
    private fun saveCartItems() {
        scope.launch {
            // Clear existing items and insert new ones
            databaseManager.deleteAllCartItems()
            _cartItems.value.forEach { cartItem ->
                databaseManager.insertCartItem(cartItem)
            }
        }
    }
} 
package com.example.enuyguncase.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enuyguncase.data.model.Product
import com.example.enuyguncase.data.repository.CartRepository
import com.example.enuyguncase.data.repository.FavoriteRepository
import com.example.enuyguncase.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val cartRepository: CartRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()
    
    private val _filteredProducts = MutableStateFlow<List<Product>>(emptyList())
    val filteredProducts: StateFlow<List<Product>> = _filteredProducts.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _totalProducts = MutableStateFlow(0)
    val totalProducts: StateFlow<Int> = _totalProducts.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()
    
    private val _sortType = MutableStateFlow(SortType.NAME_ASC)
    val sortType: StateFlow<SortType> = _sortType.asStateFlow()
    
    private val _availableCategories = MutableStateFlow<List<String>>(emptyList())
    val availableCategories: StateFlow<List<String>> = _availableCategories.asStateFlow()
    
    val cartItems = cartRepository.cartItems
    val totalCartItems = cartRepository.totalItems
    val totalCartPrice = cartRepository.totalPrice
    
    private val _cartSummary = MutableStateFlow(CartSummary(0.0, 0.0, 0.0))
    val cartSummary: StateFlow<CartSummary> = _cartSummary.asStateFlow()
    
    val favoriteItems = favoriteRepository.favoriteItems
    val totalFavorites = favoriteRepository.totalFavorites
    
    private var currentSkip = 0
    private val limit = 30
    
    init {
        loadProducts()
        setupCartSummary()
    }
    
    private fun setupCartSummary() {
        viewModelScope.launch {
            combine(
                cartRepository.cartItems,
                cartRepository.totalPrice
            ) { cartItems, totalPrice ->
                calculateCartSummary(cartItems, totalPrice)
            }.collect { summary ->
                _cartSummary.value = summary
            }
        }
    }
    
    private fun calculateCartSummary(cartItems: List<com.example.enuyguncase.data.model.CartItem>, totalPrice: Double): CartSummary {
        val subtotal = totalPrice
        val discount = calculateDiscount(subtotal)
        val total = subtotal - discount
        
        return CartSummary(
            price = subtotal,
            discount = discount,
            total = total
        )
    }
    
    private fun calculateDiscount(subtotal: Double): Double {
        return when {
            subtotal >= 500 -> subtotal * 0.15
            subtotal >= 100 -> subtotal * 0.10
            else -> 0.0
        }
    }
    
    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            repository.getProducts(limit, currentSkip).fold(
                onSuccess = { response ->
                    _products.value = response.products
                    _totalProducts.value = response.total
                    currentSkip += limit
                    
                    // Extract unique categories
                    val categories = response.products.map { it.category }.distinct().sorted()
                    _availableCategories.value = categories
                    
                    applyFilters()
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "An error occurred"
                }
            )
            
            _isLoading.value = false
        }
    }
    
    fun loadMoreProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            
            repository.getProducts(limit, currentSkip).fold(
                onSuccess = { response ->
                    val currentList = _products.value.toMutableList()
                    currentList.addAll(response.products)
                    _products.value = currentList
                    currentSkip += limit
                    
                    applyFilters()
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "An error occurred"
                }
            )
            
            _isLoading.value = false
        }
    }
    
    fun searchProducts(query: String) {
        _searchQuery.value = query
        applyFilters()
    }
    
    fun filterByCategory(category: String?) {
        _selectedCategory.value = category
        applyFilters()
    }
    
    fun sortProducts(sortType: SortType) {
        _sortType.value = sortType
        applyFilters()
    }
    
    private fun applyFilters() {
        var filteredList = _products.value.toMutableList()
        
        // Apply search filter
        val query = _searchQuery.value.lowercase()
        if (query.isNotEmpty()) {
            filteredList = filteredList.filter { product ->
                (product.title?.lowercase()?.contains(query) == true) ||
                (product.description?.lowercase()?.contains(query) == true) ||
                (product.brand?.lowercase()?.contains(query) == true)
            }.toMutableList()
        }
        
        // Apply category filter
        val selectedCategory = _selectedCategory.value
        if (selectedCategory != null) {
            filteredList = filteredList.filter { it.category == selectedCategory }.toMutableList()
        }
        
        // Apply sorting
        when (_sortType.value) {
            SortType.NAME_ASC -> filteredList.sortBy { it.title }
            SortType.NAME_DESC -> filteredList.sortByDescending { it.title }
            SortType.PRICE_ASC -> filteredList.sortBy { it.price }
            SortType.PRICE_DESC -> filteredList.sortByDescending { it.price }
        }
        
        _filteredProducts.value = filteredList
    }
    
    fun clearFilters() {
        _searchQuery.value = ""
        _selectedCategory.value = null
        _sortType.value = SortType.NAME_ASC
        applyFilters()
    }
    
    fun clearAllFiltersAndSorts() {
        _searchQuery.value = ""
        _selectedCategory.value = null
        _sortType.value = SortType.NAME_ASC
        applyFilters()
    }
    
    // Cart functions
    fun addToCart(product: Product, quantity: Int = 1) {
        cartRepository.addToCart(product, quantity)
    }
    
    fun removeFromCart(cartItemId: Int) {
        cartRepository.removeFromCart(cartItemId)
    }
    
    fun updateCartQuantity(cartItemId: Int, newQuantity: Int) {
        cartRepository.updateQuantity(cartItemId, newQuantity)
    }
    
    fun clearCart() {
        cartRepository.clearCart()
    }
    
    fun isInCart(productId: Int): Boolean {
        return cartRepository.isInCart(productId)
    }
    
    fun getCartItemQuantity(productId: Int): Int {
        return cartRepository.getCartItemQuantity(productId)
    }
    
    fun getCartItem(productId: Int): com.example.enuyguncase.data.model.CartItem? {
        return cartRepository.getCartItem(productId)
    }
    
    // Favorite functions
    fun addToFavorites(product: Product) {
        favoriteRepository.addToFavorites(product)
    }
    
    fun removeFromFavorites(productId: Int) {
        favoriteRepository.removeFromFavorites(productId)
    }
    
    fun removeFromFavoritesByItemId(itemId: Int) {
        favoriteRepository.removeFromFavoritesByItemId(itemId)
    }
    
    fun clearFavorites() {
        favoriteRepository.clearFavorites()
    }
    
    fun isInFavorites(productId: Int): Boolean {
        return favoriteRepository.isInFavorites(productId)
    }
    
    fun getFavoriteProducts(): List<Product> {
        return favoriteRepository.getFavoriteProducts()
    }

    fun getProductById(productId: Int): Product? {
        return _products.value.find { it.id == productId }
    }
    
    fun clearError() {
        _error.value = null
    }
    
    data class CartSummary(
        val price: Double,
        val discount: Double,
        val total: Double
    )
    
    enum class SortType {
        NAME_ASC,
        NAME_DESC,
        PRICE_ASC,
        PRICE_DESC
    }
} 
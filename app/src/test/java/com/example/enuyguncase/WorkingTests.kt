package com.example.enuyguncase

import com.example.enuyguncase.data.model.Product
import com.example.enuyguncase.data.model.CartItem
import com.example.enuyguncase.data.model.FavoriteItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WorkingTests {

    @Test
    fun `test product creation`() {
        val product = Product(
            id = 1,
            title = "MacBook Pro",
            description = "Powerful laptop for professionals",
            price = 1299.99,
            discountPercentage = 10.0,
            rating = 4.5,
            stock = 10,
            brand = "Apple",
            category = "Electronics",
            thumbnail = "https://example.com/macbook.jpg",
            images = listOf("https://example.com/macbook1.jpg", "https://example.com/macbook2.jpg")
        )

        assertNotNull(product)
        assertEquals(1, product.id)
        assertEquals("MacBook Pro", product.title)
        assertEquals(1299.99, product.price, 0.01)
        assertEquals("Apple", product.brand)
    }

    @Test
    fun `test cart item creation`() {
        val product = Product(
            id = 1,
            title = "iPhone 15",
            description = "Latest smartphone",
            price = 999.99,
            discountPercentage = 5.0,
            rating = 4.8,
            stock = 15,
            brand = "Apple",
            category = "Electronics",
            thumbnail = "https://example.com/iphone.jpg",
            images = listOf("https://example.com/iphone1.jpg")
        )

        val cartItem = CartItem(
            id = 1,
            product = product,
            quantity = 2
        )

        assertNotNull(cartItem)
        assertEquals(1, cartItem.id)
        assertEquals(product, cartItem.product)
        assertEquals(2, cartItem.quantity)
        assertEquals(1899.98, cartItem.totalPrice, 0.01)
    }

    @Test
    fun `test favorite item creation`() {
        // Given
        val product = Product(
            id = 2,
            title = "iPad Pro",
            description = "Professional tablet",
            price = 799.99,
            discountPercentage = 8.0,
            rating = 4.6,
            stock = 8,
            brand = "Apple",
            category = "Electronics",
            thumbnail = "https://example.com/ipad.jpg",
            images = listOf("https://example.com/ipad1.jpg")
        )

        // When
        val favoriteItem = FavoriteItem(
            id = 1,
            product = product
        )

        // Then
        assertNotNull(favoriteItem)
        assertEquals(1, favoriteItem.id)
        assertEquals(product, favoriteItem.product)
    }

    @Test
    fun `test product price calculation`() {
        // Given
        val product = Product(
            id = 3,
            title = "Samsung Galaxy",
            description = "Android smartphone",
            price = 899.99,
            discountPercentage = 15.0,
            rating = 4.4,
            stock = 12,
            brand = "Samsung",
            category = "Electronics",
            thumbnail = "https://example.com/galaxy.jpg",
            images = listOf("https://example.com/galaxy1.jpg")
        )

        // When
        val discountedPrice = product.price * (1 - product.discountPercentage / 100)

        // Then
        assertEquals(764.99, discountedPrice, 0.01) // 899.99 * 0.85
    }

    @Test
    fun `test product stock availability`() {
        // Given
        val product = Product(
            id = 4,
            title = "Dell XPS",
            description = "Windows laptop",
            price = 1499.99,
            discountPercentage = 0.0,
            rating = 4.3,
            stock = 5,
            brand = "Dell",
            category = "Electronics",
            thumbnail = "https://example.com/dell.jpg",
            images = listOf("https://example.com/dell1.jpg")
        )

        // When & Then
        assertTrue(product.stock > 0)
        assertTrue(product.stock <= 20) // Reasonable stock limit
    }

    @Test
    fun `test product rating validation`() {
        // Given
        val product = Product(
            id = 5,
            title = "HP Pavilion",
            description = "Budget laptop",
            price = 699.99,
            discountPercentage = 12.0,
            rating = 4.2,
            stock = 7,
            brand = "HP",
            category = "Electronics",
            thumbnail = "https://example.com/hp.jpg",
            images = listOf("https://example.com/hp1.jpg")
        )

        // When & Then
        assertTrue(product.rating >= 0.0)
        assertTrue(product.rating <= 5.0)
    }

    @Test
    fun `test cart item quantity validation`() {
        // Given
        val product = Product(
            id = 6,
            title = "Lenovo ThinkPad",
            description = "Business laptop",
            price = 1299.99,
            discountPercentage = 7.0,
            rating = 4.7,
            stock = 3,
            brand = "Lenovo",
            category = "Electronics",
            thumbnail = "https://example.com/lenovo.jpg",
            images = listOf("https://example.com/lenovo1.jpg")
        )

        val cartItem = CartItem(
            id = 2,
            product = product,
            quantity = 1
        )

        // When & Then
        assertTrue(cartItem.quantity > 0)
        assertTrue(cartItem.quantity <= product.stock)
    }

    @Test
    fun `test product category validation`() {
        // Given
        val validCategories = listOf("Electronics", "Clothing", "Books", "Home", "Sports")
        val product = Product(
            id = 7,
            title = "Nike Shoes",
            description = "Running shoes",
            price = 129.99,
            discountPercentage = 20.0,
            rating = 4.5,
            stock = 25,
            brand = "Nike",
            category = "Sports",
            thumbnail = "https://example.com/nike.jpg",
            images = listOf("https://example.com/nike1.jpg")
        )

        // When & Then
        assertTrue(validCategories.contains(product.category))
    }

    @Test
    fun `test product brand validation`() {
        // Given
        val product = Product(
            id = 8,
            title = "Adidas Jacket",
            description = "Sports jacket",
            price = 89.99,
            discountPercentage = 10.0,
            rating = 4.1,
            stock = 15,
            brand = "Adidas",
            category = "Clothing",
            thumbnail = "https://example.com/adidas.jpg",
            images = listOf("https://example.com/adidas1.jpg")
        )

        // When & Then
        assertNotNull(product.brand)
        assertTrue(product.brand?.isNotEmpty() == true)
    }
} 
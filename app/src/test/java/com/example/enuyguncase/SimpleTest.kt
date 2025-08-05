package com.example.enuyguncase

import com.example.enuyguncase.data.model.Product
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SimpleTest {

    @Test
    fun `test product creation`() {
        // Given
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

        // When & Then
        assertNotNull(product)
        assertEquals(1, product.id)
        assertEquals("MacBook Pro", product.title)
        assertEquals(1299.99, product.price, 0.01)
        assertEquals("Apple", product.brand)
        assertEquals("Electronics", product.category)
    }

    @Test
    fun `test product price calculation`() {
        // Given
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

        // When
        val discountedPrice = product.price * (1 - product.discountPercentage / 100)

        // Then
        assertEquals(949.99, discountedPrice, 0.01)
    }

    @Test
    fun `test product stock availability`() {
        // Given
        val product = Product(
            id = 1,
            title = "iPad Pro",
            description = "Professional tablet",
            price = 799.99,
            discountPercentage = 0.0,
            rating = 4.6,
            stock = 8,
            brand = "Apple",
            category = "Electronics",
            thumbnail = "https://example.com/ipad.jpg",
            images = listOf("https://example.com/ipad1.jpg")
        )

        // When & Then
        assertTrue(product.stock > 0)
        assertTrue(product.stock <= 100)
    }

    @Test
    fun `test product rating validation`() {
        // Given
        val product = Product(
            id = 1,
            title = "MacBook Air",
            description = "Lightweight laptop",
            price = 1099.99,
            discountPercentage = 0.0,
            rating = 4.7,
            stock = 12,
            brand = "Apple",
            category = "Electronics",
            thumbnail = "https://example.com/macbook-air.jpg",
            images = listOf("https://example.com/macbook-air1.jpg")
        )

        // When & Then
        assertTrue(product.rating >= 0.0)
        assertTrue(product.rating <= 5.0)
    }

    @Test
    fun `test product images list`() {
        // Given
        val product = Product(
            id = 1,
            title = "Apple Watch",
            description = "Smart watch",
            price = 399.99,
            discountPercentage = 0.0,
            rating = 4.4,
            stock = 20,
            brand = "Apple",
            category = "Electronics",
            thumbnail = "https://example.com/apple-watch.jpg",
            images = listOf(
                "https://example.com/apple-watch1.jpg",
                "https://example.com/apple-watch2.jpg",
                "https://example.com/apple-watch3.jpg"
            )
        )

        // When & Then
        assertNotNull(product.images)
        assertEquals(3, product.images.size)
        assertTrue(product.images.all { it.startsWith("https://") })
    }
} 
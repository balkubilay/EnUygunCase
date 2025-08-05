package com.example.enuyguncase

import com.example.enuyguncase.data.model.Product
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductTest {

    @Test
    fun `test product creation with all fields`() {
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

        // Then
        assertNotNull(product)
        assertEquals(1, product.id)
        assertEquals("MacBook Pro", product.title)
        assertEquals("Powerful laptop for professionals", product.description)
        assertEquals(1299.99, product.price, 0.01)
        assertEquals(10.0, product.discountPercentage, 0.01)
        assertEquals(4.5, product.rating, 0.01)
        assertEquals(10, product.stock)
        assertEquals("Apple", product.brand)
        assertEquals("Electronics", product.category)
        assertEquals("https://example.com/macbook.jpg", product.thumbnail)
        assertEquals(2, product.images.size)
    }

    @Test
    fun `test product with null brand`() {
        // Given
        val product = Product(
            id = 2,
            title = "Generic Laptop",
            description = "Generic laptop description",
            price = 599.99,
            discountPercentage = 5.0,
            rating = 3.8,
            stock = 5,
            brand = null,
            category = "Electronics",
            thumbnail = "https://example.com/generic.jpg",
            images = listOf("https://example.com/generic1.jpg")
        )

        // Then
        assertNotNull(product)
        assertEquals(2, product.id)
        assertEquals("Generic Laptop", product.title)
        assertEquals(null, product.brand)
        assertEquals(599.99, product.price, 0.01)
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
    fun `test product with zero discount`() {
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

        // When
        val discountedPrice = product.price * (1 - product.discountPercentage / 100)

        // Then
        assertEquals(1499.99, discountedPrice, 0.01) // No discount
    }

    @Test
    fun `test product stock validation`() {
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

        // Then
        assertTrue(product.stock > 0)
        assertTrue(product.stock <= 100) // Reasonable stock limit
    }

    @Test
    fun `test product rating validation`() {
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

        // Then
        assertTrue(product.rating >= 0.0)
        assertTrue(product.rating <= 5.0)
    }

    @Test
    fun `test product category validation`() {
        // Given
        val validCategories = listOf("Electronics", "Clothing", "Books", "Home", "Sports", "Automotive")
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

        // Then
        assertTrue(validCategories.contains(product.category))
    }

    @Test
    fun `test product images list`() {
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
            images = listOf("https://example.com/adidas1.jpg", "https://example.com/adidas2.jpg", "https://example.com/adidas3.jpg")
        )

        // Then
        assertEquals(3, product.images.size)
        assertTrue(product.images.all { it.startsWith("https://") })
    }
} 
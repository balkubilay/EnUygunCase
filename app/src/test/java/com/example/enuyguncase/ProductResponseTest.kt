package com.example.enuyguncase

import com.example.enuyguncase.data.model.Product
import com.example.enuyguncase.data.model.ProductResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductResponseTest {

    @Test
    fun `test product response creation`() {
        // Given
        val products = listOf(
            Product(
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
            ),
            Product(
                id = 2,
                title = "MacBook Pro",
                description = "Powerful laptop",
                price = 1299.99,
                discountPercentage = 10.0,
                rating = 4.5,
                stock = 10,
                brand = "Apple",
                category = "Electronics",
                thumbnail = "https://example.com/macbook.jpg",
                images = listOf("https://example.com/macbook1.jpg")
            )
        )

        // When
        val productResponse = ProductResponse(
            products = products,
            total = 2,
            skip = 0,
            limit = 30
        )

        // Then
        assertNotNull(productResponse)
        assertEquals(2, productResponse.products.size)
        assertEquals(2, productResponse.total)
        assertEquals(0, productResponse.skip)
        assertEquals(30, productResponse.limit)
    }

    @Test
    fun `test product response with empty products`() {
        // Given
        val emptyProducts = emptyList<Product>()

        // When
        val productResponse = ProductResponse(
            products = emptyProducts,
            total = 0,
            skip = 0,
            limit = 30
        )

        // Then
        assertEquals(0, productResponse.products.size)
        assertEquals(0, productResponse.total)
        assertTrue(productResponse.products.isEmpty())
    }

    @Test
    fun `test product response with pagination`() {
        // Given
        val products = listOf(
            Product(
                id = 3,
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
        )

        // When
        val productResponse = ProductResponse(
            products = products,
            total = 100,
            skip = 30,
            limit = 30
        )

        // Then
        assertEquals(1, productResponse.products.size)
        assertEquals(100, productResponse.total)
        assertEquals(30, productResponse.skip)
        assertEquals(30, productResponse.limit)
    }

    @Test
    fun `test product response with large dataset`() {
        // Given
        val largeProductList = (1..50).map { id ->
            Product(
                id = id,
                title = "Product $id",
                description = "Description for product $id",
                price = 100.0 + id,
                discountPercentage = 5.0,
                rating = 4.0,
                stock = 10,
                brand = "Brand $id",
                category = "Electronics",
                thumbnail = "https://example.com/product$id.jpg",
                images = listOf("https://example.com/product${id}1.jpg")
            )
        }

        // When
        val productResponse = ProductResponse(
            products = largeProductList,
            total = 1000,
            skip = 0,
            limit = 50
        )

        // Then
        assertEquals(50, productResponse.products.size)
        assertEquals(1000, productResponse.total)
        assertEquals(0, productResponse.skip)
        assertEquals(50, productResponse.limit)
    }

    @Test
    fun `test product response with different categories`() {
        // Given
        val products = listOf(
            Product(
                id = 4,
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
            ),
            Product(
                id = 5,
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
        )

        // When
        val productResponse = ProductResponse(
            products = products,
            total = 2,
            skip = 0,
            limit = 30
        )

        // Then
        assertEquals(2, productResponse.products.size)
        assertEquals("Sports", productResponse.products[0].category)
        assertEquals("Clothing", productResponse.products[1].category)
    }

    @Test
    fun `test product response total calculation`() {
        // Given
        val products = listOf(
            Product(
                id = 6,
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
        )

        // When
        val productResponse = ProductResponse(
            products = products,
            total = 1,
            skip = 0,
            limit = 30
        )

        // Then
        assertEquals(1, productResponse.products.size)
        assertEquals(1, productResponse.total)
        assertEquals(productResponse.products.size, productResponse.total)
    }

    @Test
    fun `test product response with skip and limit`() {
        // Given
        val products = listOf(
            Product(
                id = 7,
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
        )

        // When
        val productResponse = ProductResponse(
            products = products,
            total = 50,
            skip = 30,
            limit = 30
        )

        // Then
        assertEquals(1, productResponse.products.size)
        assertEquals(50, productResponse.total)
        assertEquals(30, productResponse.skip)
        assertEquals(30, productResponse.limit)
        assertTrue(productResponse.skip >= 0)
        assertTrue(productResponse.limit > 0)
    }
} 
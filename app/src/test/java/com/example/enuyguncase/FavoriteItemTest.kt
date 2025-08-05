package com.example.enuyguncase

import com.example.enuyguncase.data.model.FavoriteItem
import com.example.enuyguncase.data.model.Product
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FavoriteItemTest {

    @Test
    fun `test favorite item creation`() {
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
    fun `test favorite item with expensive product`() {
        // Given
        val product = Product(
            id = 2,
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

        // When
        val favoriteItem = FavoriteItem(
            id = 2,
            product = product
        )

        // Then
        assertEquals(2, favoriteItem.id)
        assertEquals("MacBook Pro", favoriteItem.product.title)
        assertEquals(1299.99, favoriteItem.product.price, 0.01)
        assertEquals("Apple", favoriteItem.product.brand)
    }

    @Test
    fun `test favorite item with cheap product`() {
        // Given
        val product = Product(
            id = 3,
            title = "USB Cable",
            description = "High quality USB cable",
            price = 9.99,
            discountPercentage = 0.0,
            rating = 4.2,
            stock = 50,
            brand = "Generic",
            category = "Electronics",
            thumbnail = "https://example.com/cable.jpg",
            images = listOf("https://example.com/cable1.jpg")
        )

        // When
        val favoriteItem = FavoriteItem(
            id = 3,
            product = product
        )

        // Then
        assertEquals(3, favoriteItem.id)
        assertEquals("USB Cable", favoriteItem.product.title)
        assertEquals(9.99, favoriteItem.product.price, 0.01)
        assertEquals("Generic", favoriteItem.product.brand)
    }

    @Test
    fun `test favorite item with null brand product`() {
        // Given
        val product = Product(
            id = 4,
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

        // When
        val favoriteItem = FavoriteItem(
            id = 4,
            product = product
        )

        // Then
        assertEquals(4, favoriteItem.id)
        assertEquals("Generic Laptop", favoriteItem.product.title)
        assertEquals(null, favoriteItem.product.brand)
    }

    @Test
    fun `test favorite item with high rating product`() {
        // Given
        val product = Product(
            id = 5,
            title = "Samsung Galaxy",
            description = "Android smartphone",
            price = 899.99,
            discountPercentage = 15.0,
            rating = 4.9,
            stock = 12,
            brand = "Samsung",
            category = "Electronics",
            thumbnail = "https://example.com/galaxy.jpg",
            images = listOf("https://example.com/galaxy1.jpg")
        )

        // When
        val favoriteItem = FavoriteItem(
            id = 5,
            product = product
        )

        // Then
        assertEquals(5, favoriteItem.id)
        assertEquals(4.9, favoriteItem.product.rating, 0.01)
        assertTrue(favoriteItem.product.rating >= 4.5) // High rating
    }

    @Test
    fun `test favorite item with low stock product`() {
        // Given
        val product = Product(
            id = 6,
            title = "Limited Edition Watch",
            description = "Limited edition luxury watch",
            price = 2999.99,
            discountPercentage = 0.0,
            rating = 4.7,
            stock = 1,
            brand = "Luxury Brand",
            category = "Jewelry",
            thumbnail = "https://example.com/watch.jpg",
            images = listOf("https://example.com/watch1.jpg")
        )

        // When
        val favoriteItem = FavoriteItem(
            id = 6,
            product = product
        )

        // Then
        assertEquals(6, favoriteItem.id)
        assertEquals(1, favoriteItem.product.stock)
        assertTrue(favoriteItem.product.stock <= 5) // Low stock
    }

    @Test
    fun `test favorite item with multiple images`() {
        // Given
        val product = Product(
            id = 7,
            title = "Camera Kit",
            description = "Professional camera kit",
            price = 1499.99,
            discountPercentage = 8.0,
            rating = 4.6,
            stock = 8,
            brand = "Canon",
            category = "Electronics",
            thumbnail = "https://example.com/camera.jpg",
            images = listOf("https://example.com/camera1.jpg", "https://example.com/camera2.jpg", "https://example.com/camera3.jpg", "https://example.com/camera4.jpg")
        )

        // When
        val favoriteItem = FavoriteItem(
            id = 7,
            product = product
        )

        // Then
        assertEquals(7, favoriteItem.id)
        assertEquals(4, favoriteItem.product.images.size)
        assertTrue(favoriteItem.product.images.all { it.startsWith("https://") })
    }

    @Test
    fun `test favorite item product reference integrity`() {
        // Given
        val product = Product(
            id = 8,
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
            id = 8,
            product = product
        )

        // Then
        assertEquals(product.id, favoriteItem.product.id)
        assertEquals(product.title, favoriteItem.product.title)
        assertEquals(product.description, favoriteItem.product.description)
        assertEquals(product.price, favoriteItem.product.price, 0.01)
        assertEquals(product.discountPercentage, favoriteItem.product.discountPercentage, 0.01)
        assertEquals(product.rating, favoriteItem.product.rating, 0.01)
        assertEquals(product.stock, favoriteItem.product.stock)
        assertEquals(product.brand, favoriteItem.product.brand)
        assertEquals(product.category, favoriteItem.product.category)
        assertEquals(product.thumbnail, favoriteItem.product.thumbnail)
        assertEquals(product.images, favoriteItem.product.images)
    }
} 
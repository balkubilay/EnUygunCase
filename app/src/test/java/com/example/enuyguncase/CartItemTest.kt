package com.example.enuyguncase

import com.example.enuyguncase.data.model.CartItem
import com.example.enuyguncase.data.model.Product
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class CartItemTest {

    @Test
    fun `test cart item creation`() {
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
        val cartItem = CartItem(
            id = 1,
            product = product,
            quantity = 2
        )

        // Then
        assertNotNull(cartItem)
        assertEquals(1, cartItem.id)
        assertEquals(product, cartItem.product)
        assertEquals(2, cartItem.quantity)
    }

    @Test
    fun `test cart item total price calculation`() {
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

        val cartItem = CartItem(
            id = 2,
            product = product,
            quantity = 3
        )

        // When
        val expectedTotal = product.price * 3 * (1 - product.discountPercentage / 100)

        // Then
        assertEquals(expectedTotal, cartItem.totalPrice, 0.01)
        assertEquals(2207.97, cartItem.totalPrice, 0.01) // 799.99 * 3 * 0.92
    }

    @Test
    fun `test cart item with quantity one`() {
        // Given
        val product = Product(
            id = 3,
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

        // When
        val cartItem = CartItem(
            id = 3,
            product = product,
            quantity = 1
        )

        // Then
        assertEquals(1, cartItem.quantity)
        assertEquals(1169.99, cartItem.totalPrice, 0.01) // 1299.99 * 0.90
    }

    @Test
    fun `test cart item with zero price product`() {
        // Given
        val product = Product(
            id = 4,
            title = "Free Sample",
            description = "Free product sample",
            price = 0.0,
            discountPercentage = 0.0,
            rating = 4.0,
            stock = 100,
            brand = "Sample Brand",
            category = "Samples",
            thumbnail = "https://example.com/sample.jpg",
            images = listOf("https://example.com/sample1.jpg")
        )

        // When
        val cartItem = CartItem(
            id = 4,
            product = product,
            quantity = 5
        )

        // Then
        assertEquals(0.0, cartItem.totalPrice, 0.01)
        assertEquals(5, cartItem.quantity)
    }

    @Test
    fun `test cart item with high quantity`() {
        // Given
        val product = Product(
            id = 5,
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
        val cartItem = CartItem(
            id = 5,
            product = product,
            quantity = 10
        )

        // Then
        assertEquals(99.90, cartItem.totalPrice, 0.01) // 9.99 * 10
        assertEquals(10, cartItem.quantity)
    }

    @Test
    fun `test cart item product reference`() {
        // Given
        val product = Product(
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

        // When
        val cartItem = CartItem(
            id = 6,
            product = product,
            quantity = 1
        )

        // Then
        assertEquals(product.id, cartItem.product.id)
        assertEquals(product.title, cartItem.product.title)
        assertEquals(product.price, cartItem.product.price, 0.01)
    }
} 
package com.example.enuyguncase

import org.junit.Test
import org.junit.Assert.*
import com.google.common.truth.Truth.assertThat

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    
    @Test
    fun `test basic assertions`() {
        // Given
        val expected = "Hello World"
        
        // When
        val actual = "Hello World"
        
        // Then
        assertThat(actual).isEqualTo(expected)
    }
    
    @Test
    fun `test list operations`() {
        // Given
        val list = listOf(1, 2, 3, 4, 5)
        
        // When
        val filtered = list.filter { it > 2 }
        val sum = list.sum()
        
        // Then
        assertThat(filtered).hasSize(3)
        assertThat(sum).isEqualTo(15)
    }
    
    @Test
    fun `test string operations`() {
        // Given
        val text = "hello world"
        
        // When
        val uppercase = text.uppercase()
        val wordCount = text.split(" ").size
        
        // Then
        assertThat(uppercase).isEqualTo("HELLO WORLD")
        assertThat(wordCount).isEqualTo(2)
    }
}
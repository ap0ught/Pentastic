package app.pentastic.db

import kotlin.test.Test
import kotlin.test.assertEquals

class StringListTypeConverterTest {

    @Test
    fun testStringListTypeConverter_Roundtrip() {
        val originalList = listOf("apple", "banana", "cherry")
        val jsonString = StringListTypeConverter.fromList(originalList)
        val decodedList = StringListTypeConverter.fromString(jsonString)

        assertEquals(originalList, decodedList)
    }

    @Test
    fun testStringListTypeConverter_EmptyList() {
        val originalList = emptyList<String>()
        val jsonString = StringListTypeConverter.fromList(originalList)
        val decodedList = StringListTypeConverter.fromString(jsonString)

        assertEquals(originalList, decodedList)
    }
}

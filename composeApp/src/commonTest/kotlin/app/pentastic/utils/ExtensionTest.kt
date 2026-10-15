package app.pentastic.utils

import app.pentastic.data.RepeatFrequency
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class ExtensionTest {

    @Test
    fun testNextRepeatDate_None() {
        val start = LocalDate(2026, 10, 15)
        assertEquals(start, nextRepeatDate(start, RepeatFrequency.NONE))
    }

    @Test
    fun testNextRepeatDate_Daily() {
        val start = LocalDate(2026, 10, 15)
        val expected = LocalDate(2026, 10, 16)
        assertEquals(expected, nextRepeatDate(start, RepeatFrequency.DAILY))
    }

    @Test
    fun testNextRepeatDate_Weekly() {
        val start = LocalDate(2026, 10, 15)
        val expected = LocalDate(2026, 10, 22)
        assertEquals(expected, nextRepeatDate(start, RepeatFrequency.WEEKLY))
    }

    @Test
    fun testNextRepeatDate_Monthly() {
        val start = LocalDate(2026, 10, 15)
        val expected = LocalDate(2026, 11, 15)
        assertEquals(expected, nextRepeatDate(start, RepeatFrequency.MONTHLY))
    }

    @Test
    fun testNextRepeatDate_Monthly_MonthEndClamping() {
        // Jan 31 + 1 month in non-leap year (2026) -> Feb 28
        val start = LocalDate(2026, 1, 31)
        val expected = LocalDate(2026, 2, 28)
        assertEquals(expected, nextRepeatDate(start, RepeatFrequency.MONTHLY))
    }

    @Test
    fun testNextRepeatDate_Quarterly() {
        val start = LocalDate(2026, 1, 15)
        val expected = LocalDate(2026, 4, 15)
        assertEquals(expected, nextRepeatDate(start, RepeatFrequency.QUARTERLY))
    }

    @Test
    fun testNextRepeatDate_Yearly() {
        val start = LocalDate(2026, 10, 15)
        val expected = LocalDate(2027, 10, 15)
        assertEquals(expected, nextRepeatDate(start, RepeatFrequency.YEARLY))
    }
}

package app.pentastic.data

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DueDateTest {

    private val timeZone = TimeZone.UTC

    @Test
    fun testResolveRange_Today() {
        val today = LocalDate(2026, 10, 15)
        val range = DueDateOption.TODAY.resolveRange(today)
        assertNotNull(range)
        assertEquals(today, range.first)
        assertEquals(today, range.second)
    }

    @Test
    fun testResolveRange_Tomorrow() {
        val today = LocalDate(2026, 10, 15)
        val tomorrow = LocalDate(2026, 10, 16)
        val range = DueDateOption.TOMORROW.resolveRange(today)
        assertNotNull(range)
        assertEquals(tomorrow, range.first)
        assertEquals(tomorrow, range.second)
    }

    @Test
    fun testResolveRange_ThisWeek() {
        // 2026-10-15 is Thursday (isoDayNumber = 4)
        // Week starts Monday 2026-10-12, ends Sunday 2026-10-18
        val today = LocalDate(2026, 10, 15)
        val range = DueDateOption.THIS_WEEK.resolveRange(today)
        assertNotNull(range)
        assertEquals(LocalDate(2026, 10, 12), range.first)
        assertEquals(LocalDate(2026, 10, 18), range.second)
    }

    @Test
    fun testResolveRange_NextWeek() {
        // Thursday 2026-10-15 -> Next week: Mon 2026-10-19 to Sun 2026-10-25
        val today = LocalDate(2026, 10, 15)
        val range = DueDateOption.NEXT_WEEK.resolveRange(today)
        assertNotNull(range)
        assertEquals(LocalDate(2026, 10, 19), range.first)
        assertEquals(LocalDate(2026, 10, 25), range.second)
    }

    @Test
    fun testResolveRange_ThisMonth() {
        val today = LocalDate(2026, 10, 15)
        val range = DueDateOption.THIS_MONTH.resolveRange(today)
        assertNotNull(range)
        assertEquals(LocalDate(2026, 10, 1), range.first)
        assertEquals(LocalDate(2026, 10, 31), range.second)
    }

    @Test
    fun testResolveRange_NextMonth() {
        val today = LocalDate(2026, 10, 15)
        val range = DueDateOption.NEXT_MONTH.resolveRange(today)
        assertNotNull(range)
        assertEquals(LocalDate(2026, 11, 1), range.first)
        assertEquals(LocalDate(2026, 11, 30), range.second)
    }

    @Test
    fun testResolveRange_ThisYear() {
        val today = LocalDate(2026, 10, 15)
        val range = DueDateOption.THIS_YEAR.resolveRange(today)
        assertNotNull(range)
        assertEquals(LocalDate(2026, 1, 1), range.first)
        assertEquals(LocalDate(2026, 12, 31), range.second)
    }

    @Test
    fun testResolveRange_NextYear() {
        val today = LocalDate(2026, 10, 15)
        val range = DueDateOption.NEXT_YEAR.resolveRange(today)
        assertNotNull(range)
        assertEquals(LocalDate(2027, 1, 1), range.first)
        assertEquals(LocalDate(2027, 12, 31), range.second)
    }

    @Test
    fun testResolveRange_SomedayAndCustom_returnsNull() {
        val today = LocalDate(2026, 10, 15)
        assertNull(DueDateOption.SOMEDAY.resolveRange(today))
        assertNull(DueDateOption.CUSTOM.resolveRange(today))
    }

    @Test
    fun testEpochDaysConversion() {
        val date = LocalDate(2026, 10, 15)
        val dueValue = date.toDueValue()
        val restoredDate = dueValueToLocalDate(dueValue, timeZone)
        assertEquals(date, restoredDate)
    }

    @Test
    fun testFormatDueDateLabel_Someday() {
        val label = formatDueDateLabel(DUE_SOMEDAY, DUE_SOMEDAY, timeZone)
        assertEquals("Someday", label)
    }

    @Test
    fun testFormatDueDateLabel_SameDay() {
        val date = LocalDate(2026, 10, 15)
        val value = date.toDueValue()
        val label = formatDueDateLabel(value, value, timeZone)
        assertTrue(label.startsWith("15 Oct"))
    }

    @Test
    fun testFormatDueDateLabel_SameMonthRange() {
        val start = LocalDate(2026, 10, 15).toDueValue()
        val end = LocalDate(2026, 10, 20).toDueValue()
        val label = formatDueDateLabel(start, end, timeZone)
        assertTrue(label.startsWith("15–20 Oct"))
    }

    @Test
    fun testFormatDueDateLabel_DifferentMonthRange() {
        val start = LocalDate(2026, 10, 15).toDueValue()
        val end = LocalDate(2026, 11, 2).toDueValue()
        val label = formatDueDateLabel(start, end, timeZone)
        assertTrue(label.contains("15 Oct – 2 Nov"))
    }

    @Test
    fun testNoteDueDateHelpers() {
        val dateValue = LocalDate(2026, 10, 15).toDueValue()
        val noteWithDue = Note(id = 1L, text = "test", dueStartAt = dateValue, dueEndAt = dateValue)
        val noteWithoutDue = Note(id = 2L, text = "test", dueStartAt = 0L, dueEndAt = 0L)
        val noteSomeday = Note(id = 3L, text = "test", dueStartAt = DUE_SOMEDAY, dueEndAt = DUE_SOMEDAY)

        assertTrue(noteWithDue.hasDueDate)
        assertFalse(noteWithoutDue.hasDueDate)

        assertTrue(noteSomeday.isDueSomeday)
        assertFalse(noteWithDue.isDueSomeday)
    }
}

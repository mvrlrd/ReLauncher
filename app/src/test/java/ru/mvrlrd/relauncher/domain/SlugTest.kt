package ru.mvrlrd.relauncher.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class SlugTest {

    @Test
    fun lowercasesSingleWord() {
        assertEquals("telegram", slugify("Telegram"))
    }

    @Test
    fun replacesSpacesWithUnderscore() {
        assertEquals("google_maps", slugify("Google Maps"))
    }

    @Test
    fun replacesNonAlphanumericWithUnderscore() {
        assertEquals("at_t", slugify("AT&T"))
    }

    @Test
    fun collapsesRepeatedSeparators() {
        assertEquals("a_b", slugify("a  -  b"))
    }

    @Test
    fun trimsLeadingAndTrailingSeparators() {
        assertEquals("app", slugify("  app!  "))
    }

    @Test
    fun emptyForOnlySymbols() {
        assertEquals("", slugify("!!!"))
        assertEquals("", slugify(""))
    }

    @Test
    fun keepsDigits() {
        assertEquals("app2", slugify("App2"))
    }
}

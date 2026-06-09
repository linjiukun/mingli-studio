package com.mingli.fortune.utils;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ZodiacCalculatorTest {

    @Test
    void shouldCalculateWesternZodiacBoundaryDates() {
        assertEquals(0, ZodiacCalculator.getZodiacIndex(3, 21));
        assertEquals(0, ZodiacCalculator.getZodiacIndex(4, 19));
        assertEquals(1, ZodiacCalculator.getZodiacIndex(4, 20));
        assertEquals(1, ZodiacCalculator.getZodiacIndex(5, 20));
        assertEquals(2, ZodiacCalculator.getZodiacIndex(5, 21));
        assertEquals(2, ZodiacCalculator.getZodiacIndex(6, 21));
        assertEquals(3, ZodiacCalculator.getZodiacIndex(6, 22));
        assertEquals(3, ZodiacCalculator.getZodiacIndex(7, 22));
        assertEquals(4, ZodiacCalculator.getZodiacIndex(7, 23));
        assertEquals(4, ZodiacCalculator.getZodiacIndex(8, 22));
        assertEquals(5, ZodiacCalculator.getZodiacIndex(8, 23));
        assertEquals(5, ZodiacCalculator.getZodiacIndex(9, 22));
        assertEquals(6, ZodiacCalculator.getZodiacIndex(9, 23));
        assertEquals(6, ZodiacCalculator.getZodiacIndex(10, 23));
        assertEquals(7, ZodiacCalculator.getZodiacIndex(10, 24));
        assertEquals(7, ZodiacCalculator.getZodiacIndex(11, 22));
        assertEquals(8, ZodiacCalculator.getZodiacIndex(11, 23));
        assertEquals(8, ZodiacCalculator.getZodiacIndex(12, 21));
        assertEquals(9, ZodiacCalculator.getZodiacIndex(12, 22));
        assertEquals(9, ZodiacCalculator.getZodiacIndex(1, 19));
        assertEquals(10, ZodiacCalculator.getZodiacIndex(1, 20));
        assertEquals(10, ZodiacCalculator.getZodiacIndex(2, 18));
        assertEquals(11, ZodiacCalculator.getZodiacIndex(2, 19));
        assertEquals(11, ZodiacCalculator.getZodiacIndex(3, 20));
    }

    @Test
    void shouldRejectInvalidMonthDay() {
        assertEquals(-1, ZodiacCalculator.getZodiacIndex(0, 1));
        assertEquals(-1, ZodiacCalculator.getZodiacIndex(13, 1));
        assertEquals(-1, ZodiacCalculator.getZodiacIndex(1, 0));
        assertEquals(-1, ZodiacCalculator.getZodiacIndex(1, 32));
        assertEquals(-1, ZodiacCalculator.getZodiacIndex(2, 30));
        assertEquals(-1, ZodiacCalculator.getZodiacIndex(2, 31));
        assertEquals(-1, ZodiacCalculator.getZodiacIndex(4, 31));
        assertEquals(-1, ZodiacCalculator.getZodiacIndex(6, 31));
        assertEquals(-1, ZodiacCalculator.getZodiacIndex(9, 31));
        assertEquals(-1, ZodiacCalculator.getZodiacIndex(11, 31));
    }

    @Test
    void shouldAllowFebruary29BecauseYearIsUnknown() {
        assertEquals(11, ZodiacCalculator.getZodiacIndex(2, 29));
    }

    @Test
    void weeklyHoroscopeShouldUseSameDefaultWeekSeedDuringSameIsoWeek() {
        Map<String, Object> monday = ZodiacCalculator.generateWeeklyHoroscope(0, "2026-W24");
        Map<String, Object> friday = ZodiacCalculator.generateWeeklyHoroscope(0, "2026-W24");
        assertEquals(monday, friday);
    }
}

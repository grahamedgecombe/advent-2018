package com.grahamedgecombe.advent2018;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class Day1Test {
	@Test
	public void testPart1() {
		assertEquals(3, Day1.getFrequency(Arrays.asList("+1", "-2", "+3", "+1")));
		assertEquals(3, Day1.getFrequency(Arrays.asList("+1", "+1", "+1")));
		assertEquals(0, Day1.getFrequency(Arrays.asList("+1", "+1", "-2")));
		assertEquals(-6, Day1.getFrequency(Arrays.asList("-1", "-2", "-3")));
	}

	@Test
	public void testPart2() {
		assertEquals(2, Day1.getFrequencySeenTwice(Arrays.asList("+1", "-2", "+3", "+1")));
		assertEquals(0, Day1.getFrequencySeenTwice(Arrays.asList("+1", "-1")));
		assertEquals(10, Day1.getFrequencySeenTwice(Arrays.asList("+3", "+3", "+4", "-2", "-4")));
		assertEquals(5, Day1.getFrequencySeenTwice(Arrays.asList("-6", "+3", "+8", "+5", "-6")));
		assertEquals(14, Day1.getFrequencySeenTwice(Arrays.asList("+7", "+7", "-2", "-7", "-4")));
	}
}

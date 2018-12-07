package com.grahamedgecombe.advent2018;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class Day6Test {
	@Test
	public void testPart1() {
		assertEquals(17, Day6.getAreas(Day6.parsePoints(Arrays.asList(
			"1, 1",
			"1, 6",
			"8, 3",
			"3, 4",
			"5, 5",
			"8, 9"
		)), 32).getMaxArea());
	}

	@Test
	public void testPart2() {
		assertEquals(16, Day6.getAreas(Day6.parsePoints(Arrays.asList(
			"1, 1",
			"1, 6",
			"8, 3",
			"3, 4",
			"5, 5",
			"8, 9"
		)), 32).getSafeArea());
	}
}

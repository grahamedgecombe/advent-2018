package com.grahamedgecombe.advent2018;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class Day2Test {
	@Test
	public void testPart1() {
		assertEquals(12, Day2.getChecksum(Arrays.asList(
			"abcdef",
			"bababc",
			"abbcde",
			"abcccd",
			"aabcdd",
			"abcdee",
			"ababab"
		)));
	}

	@Test
	public void testPart2() {
		assertEquals("fgij", Day2.getCorrectBoxes(Arrays.asList(
			"abcde",
			"fghij",
			"klmno",
			"pqrst",
			"fguij",
			"axcye",
			"wvxyz"
		)));
	}
}

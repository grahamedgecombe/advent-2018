package com.grahamedgecombe.advent2018;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class Day3Test {
	@Test
	public void testPart1() {
		assertEquals(4, Day3.getOverlappingClaims(Day3.parseClaims(Arrays.asList(
			"#1 @ 1,3: 4x4",
			"#2 @ 3,1: 4x4",
			"#3 @ 5,5: 2x2"
		))));
	}
}

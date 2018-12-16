package com.grahamedgecombe.advent2018;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class Day16Test {
	@Test
	public void testPart1() {
		assertEquals(1, Day16.countMatchingSamples(Day16.Sample.parse("Before: [3, 2, 1, 1]\n" +
			"9 2 1 2\n" +
			"After:  [3, 2, 2, 1]")));
	}
}

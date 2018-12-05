package com.grahamedgecombe.advent2018;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class Day5Test {
	@Test
	public void testPart1() {
		assertEquals(0, Day5.react("aA"));
		assertEquals(0, Day5.react("abBA"));
		assertEquals(4, Day5.react("abAB"));
		assertEquals(6, Day5.react("aabAAB"));
		assertEquals(10, Day5.react("dabAcCaCBAcCcaDA"));
	}

	@Test
	public void testPart2() {
		assertEquals(4, Day5.bestReact("dabAcCaCBAcCcaDA"));
	}
}

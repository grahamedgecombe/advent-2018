package com.grahamedgecombe.advent2018;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class Day9Test {
	@Test
	public void testPart1() {
		assertEquals(32, Day9.getHighScore(9, 25));
		assertEquals(8317, Day9.getHighScore(10, 1618));
		assertEquals(146373, Day9.getHighScore(13, 7999));
		assertEquals(2764, Day9.getHighScore(17, 1104));
		assertEquals(54718, Day9.getHighScore(21, 6111));
		assertEquals(37305, Day9.getHighScore(30, 5807));
	}
}

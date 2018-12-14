package com.grahamedgecombe.advent2018;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class Day14Test {
	@Test
	public void testPart1() {
		assertEquals("5158916779", Day14.getScores(9));
		assertEquals("0124515891", Day14.getScores(5));
		assertEquals("9251071085", Day14.getScores(18));
		assertEquals("5941429882", Day14.getScores(2018));
	}
}

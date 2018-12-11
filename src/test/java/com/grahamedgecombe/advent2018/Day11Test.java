package com.grahamedgecombe.advent2018;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class Day11Test {
	@Test
	public void testPart1() {
		assertEquals(4, Day11.getPower(3, 5, 8));
		assertEquals(-5, Day11.getPower(122, 79, 57));
		assertEquals(0, Day11.getPower(217, 196, 39));
		assertEquals(4, Day11.getPower(101, 153, 71));

		assertEquals("33,45", Day11.getMostPowerful3x3Square(18));
		assertEquals("21,61", Day11.getMostPowerful3x3Square(42));
	}

	@Test
	public void testPart2() {
		assertEquals("90,269,16", Day11.getMostPowerfulSquare(18));
		assertEquals("232,251,12", Day11.getMostPowerfulSquare(42));
	}
}

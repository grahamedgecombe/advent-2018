package com.grahamedgecombe.advent2018;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class Day18Test {
	@Test
	public void testPart1() {
		assertEquals(1147, Day18.Grid.parse(Arrays.asList(
			".#.#...|#.",
			".....#|##|",
			".|..|...#.",
			"..|#.....#",
			"#.#|||#|#|",
			"...#.||...",
			".|....|...",
			"||...#|.#|",
			"|.||||..|.",
			"...#.|..|."
		)).getResourceValue(10));
	}
}

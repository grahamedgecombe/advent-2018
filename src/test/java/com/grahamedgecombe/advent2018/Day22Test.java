package com.grahamedgecombe.advent2018;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class Day22Test {
	@Test
	public void testPart1() {
		Day22.Grid grid = new Day22.Grid(510, 10, 10);

		Day22.Position pos = new Day22.Position(0, 0);
		assertEquals(0, grid.getGeologicalIndex(pos));
		assertEquals(510, grid.getErosionLevel(pos));
		assertEquals(0, grid.getRisk(pos));

		pos = new Day22.Position(1, 0);
		assertEquals(16807, grid.getGeologicalIndex(pos));
		assertEquals(17317, grid.getErosionLevel(pos));
		assertEquals(1, grid.getRisk(pos));

		pos = new Day22.Position(0, 1);
		assertEquals(48271, grid.getGeologicalIndex(pos));
		assertEquals(8415, grid.getErosionLevel(pos));
		assertEquals(0, grid.getRisk(pos));

		pos = new Day22.Position(1, 1);
		assertEquals(145722555, grid.getGeologicalIndex(pos));
		assertEquals(1805, grid.getErosionLevel(pos));
		assertEquals(2, grid.getRisk(pos));

		pos = new Day22.Position(10, 10);
		assertEquals(0, grid.getGeologicalIndex(pos));
		assertEquals(510, grid.getErosionLevel(pos));
		assertEquals(0, grid.getRisk(pos));

		assertEquals(114, grid.sumRisk());
	}

	@Test
	public void testPart2() {
		Day22.Grid grid = new Day22.Grid(510, 10, 10);
		assertEquals(45, grid.getMinutes());
	}
}

package com.grahamedgecombe.advent2018;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class Day17Test {
	@Test
	public void testPart1() {
		Day17.Grid grid = Day17.Grid.parse(Arrays.asList(
			"x=495, y=2..7",
			"y=7, x=495..501",
			"x=501, y=3..7",
			"x=498, y=2..4",
			"x=506, y=1..2",
			"x=498, y=10..13",
			"x=504, y=10..13",
			"y=13, x=498..504"
		));
		grid.flow();
		assertEquals(57, grid.getWaterTiles());
	}

	@Test
	public void testPart2() {
		Day17.Grid grid = Day17.Grid.parse(Arrays.asList(
			"x=495, y=2..7",
			"y=7, x=495..501",
			"x=501, y=3..7",
			"x=498, y=2..4",
			"x=506, y=1..2",
			"x=498, y=10..13",
			"x=504, y=10..13",
			"y=13, x=498..504"
		));
		grid.flow();
		assertEquals(29, grid.getStillWaterTiles());
	}
}

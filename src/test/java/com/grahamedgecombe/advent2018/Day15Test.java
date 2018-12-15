package com.grahamedgecombe.advent2018;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class Day15Test {
	@Test
	public void testPart1() {
		assertEquals(36334, Day15.Map.parse(Arrays.asList(
			"#######",
			"#G..#E#",
			"#E#E.E#",
			"#G.##.#",
			"#...#E#",
			"#...E.#",
			"#######"
		)).getOutcome());

		assertEquals(39514, Day15.Map.parse(Arrays.asList(
			"#######",
			"#E..EG#",
			"#.#G.E#",
			"#E.##E#",
			"#G..#.#",
			"#..E#.#",
			"#######"
		)).getOutcome());

		assertEquals(27755, Day15.Map.parse(Arrays.asList(
			"#######",
			"#E.G#.#",
			"#.#G..#",
			"#G.#.G#",
			"#G..#.#",
			"#...E.#",
			"#######"
		)).getOutcome());

		assertEquals(28944, Day15.Map.parse(Arrays.asList(
			"#######",
			"#.E...#",
			"#.#..G#",
			"#.###.#",
			"#E#G#G#",
			"#...#G#",
			"#######"
		)).getOutcome());

		assertEquals(18740, Day15.Map.parse(Arrays.asList(
			"#########",
			"#G......#",
			"#.E.#...#",
			"#..##..G#",
			"#...##..#",
			"#...#...#",
			"#.G...G.#",
			"#.....G.#",
			"#########"
		)).getOutcome());
	}
}

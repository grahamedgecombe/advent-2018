package com.grahamedgecombe.advent2018;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class Day12Test {
	@Test
	public void testPart1() {
		assertEquals(325, Day12.Pots.parse(Arrays.asList(
			"initial state: #..#.#..##......###...###",
			"",
			"...## => #",
			"..#.. => #",
			".#... => #",
			".#.#. => #",
			".#.## => #",
			".##.. => #",
			".#### => #",
			"#.#.# => #",
			"#.### => #",
			"##.#. => #",
			"##.## => #",
			"###.. => #",
			"###.# => #",
			"####. => #"
		)).getSum(20));
	}
}

package com.grahamedgecombe.advent2018;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class Day13Test {
	@Test
	public void testPart1() {
		assertEquals("7,3", Day13.Grid.parse(Arrays.asList(
			"/->-\\        ",
			"|   |  /----\\",
			"| /-+--+-\\  |",
			"| | |  | v  |",
			"\\-+-/  \\-+--/",
			"  \\------/   "
		)).getFirstCrashLocation());
	}

	@Test
	public void testPart2() {
		assertEquals("6,4", Day13.Grid.parse(Arrays.asList(
			"/>-<\\  ",
			"|   |  ",
			"| /<+-\\",
			"| | | v",
			"\\>+</ |",
			"  |   ^",
			"  \\<->/"
		)).getLastCartLocation());
	}
}

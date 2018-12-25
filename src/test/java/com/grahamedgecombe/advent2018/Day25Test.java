package com.grahamedgecombe.advent2018;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class Day25Test {
	@Test
	public void testPart1() {
		assertEquals(2, Day25.getConstellations(Arrays.asList(
			"0,0,0,0",
			"3,0,0,0",
			"0,3,0,0",
			"0,0,3,0",
			"0,0,0,3",
			"0,0,0,6",
			"9,0,0,0",
			"12,0,0,0"
		)));

		assertEquals(4, Day25.getConstellations(Arrays.asList(
			"-1,2,2,0",
			"0,0,2,-2",
			"0,0,0,-2",
			"-1,2,0,0",
			"-2,-2,-2,2",
			"3,0,2,-1",
			"-1,3,2,2",
			"-1,0,-1,0",
			"0,2,1,-2",
			"3,0,0,0"
		)));

		assertEquals(3, Day25.getConstellations(Arrays.asList(
			"1,-1,0,1",
			"2,0,-1,0",
			"3,2,-1,0",
			"0,0,3,1",
			"0,0,-1,-1",
			"2,3,-2,0",
			"-2,2,0,0",
			"2,-2,0,-1",
			"1,-1,0,-1",
			"3,2,0,2"
		)));

		assertEquals(8, Day25.getConstellations(Arrays.asList(
			"1,-1,-1,-2",
			"-2,-2,0,1",
			"0,2,1,3",
			"-2,3,-2,1",
			"0,2,3,-2",
			"-1,-1,1,-2",
			"0,-2,-1,0",
			"-2,2,3,-1",
			"1,2,2,0",
			"-1,-2,0,-2"
		)));
	}
}

package com.grahamedgecombe.advent2018;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class Day23Test {
	@Test
	public void testPart1() {
		assertEquals(7, Day23.getNanobotsInRange(Day23.Nanobot.parse(Arrays.asList(
			"pos=<0,0,0>, r=4",
			"pos=<1,0,0>, r=1",
			"pos=<4,0,0>, r=3",
			"pos=<0,2,0>, r=1",
			"pos=<0,5,0>, r=3",
			"pos=<0,0,3>, r=1",
			"pos=<1,1,1>, r=1",
			"pos=<1,1,2>, r=1",
			"pos=<1,3,1>, r=1"
		))));
	}
}

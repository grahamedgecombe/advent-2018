package com.grahamedgecombe.advent2018;

import java.io.IOException;
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

	@Test
	public void testPart2() throws IOException {
		assertEquals("36", Day23.getDistance(Day23.Nanobot.parse(Arrays.asList(
			"pos=<10,12,12>, r=2",
			"pos=<12,14,12>, r=2",
			"pos=<16,12,12>, r=4",
			"pos=<14,14,14>, r=6",
			"pos=<50,50,50>, r=200",
			"pos=<10,10,10>, r=5"
		))));
	}
}

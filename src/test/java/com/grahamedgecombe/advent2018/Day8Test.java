package com.grahamedgecombe.advent2018;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class Day8Test {
	@Test
	public void testPart1() {
		assertEquals(138, Day8.parseTree("2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2").sumMetadata());
	}
}

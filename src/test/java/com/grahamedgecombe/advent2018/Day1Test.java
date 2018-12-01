package com.grahamedgecombe.advent2018;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class Day1Test {
	@Test
	public void testPart1() {
		assertEquals(3, Day1.getFrequency(Arrays.asList("+1", "-2", "+3", "+1")));
		assertEquals(3, Day1.getFrequency(Arrays.asList("+1", "+1", "+1")));
		assertEquals(0, Day1.getFrequency(Arrays.asList("+1", "+1", "-2")));
		assertEquals(-6, Day1.getFrequency(Arrays.asList("-1", "-2", "-3")));
	}
}

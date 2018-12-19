package com.grahamedgecombe.advent2018;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class Day19Test {
	@Test
	public void testPart1() {
		assertEquals(7, Day19.Program.parse(Arrays.asList(
			"#ip 0",
			"seti 5 0 1",
			"seti 6 0 2",
			"addi 0 1 0",
			"addr 1 2 3",
			"setr 1 0 0",
			"seti 8 0 4",
			"seti 9 0 5"
		)).evaluate());
	}
}

package com.grahamedgecombe.advent2018;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class Day7Test {
	@Test
	public void testPart1() {
		assertEquals("CABDFE", Day7.getOrder(Day7.parseSteps(Arrays.asList(
			"Step C must be finished before step A can begin.",
			"Step C must be finished before step F can begin.",
			"Step A must be finished before step B can begin.",
			"Step A must be finished before step D can begin.",
			"Step B must be finished before step E can begin.",
			"Step D must be finished before step E can begin.",
			"Step F must be finished before step E can begin."
		))));
	}
}

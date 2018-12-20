package com.grahamedgecombe.advent2018;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class Day20Test {
	@Test
	public void testPart1() {
		assertEquals(3, new Day20.Graph("^WNE$").getFurthestRoom());
		assertEquals(10, new Day20.Graph("^ENWWW(NEEE|SSE(EE|N))$").getFurthestRoom());
		assertEquals(18, new Day20.Graph("^ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN$").getFurthestRoom());
		assertEquals(23, new Day20.Graph("^ESSWWN(E|NNENN(EESS(WNSE|)SSS|WWWSSSSE(SW|NNNE)))$").getFurthestRoom());
		assertEquals(31, new Day20.Graph("^WSSEESWWWNW(S|NENNEEEENN(ESSSSW(NWSW|SSEN)|WSWWN(E|WWS(E|SS))))$").getFurthestRoom());
	}
}

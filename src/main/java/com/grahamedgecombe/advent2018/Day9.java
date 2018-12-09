package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Day9 {
	private static final Pattern PATTERN = Pattern.compile("^([0-9]+) players; last marble is worth ([0-9]+) points$");

	public static void main(String[] args) throws IOException {
		Matcher matcher = PATTERN.matcher(AdventUtils.readString("day9.txt"));
		if (!matcher.matches()) {
			throw new IllegalArgumentException();
		}
		int players = Integer.parseInt(matcher.group(1));
		int lastMarble = Integer.parseInt(matcher.group(2));
		System.out.println(getHighScore(players, lastMarble));
	}

	public static int getHighScore(int players, int lastMarble) {
		List<Integer> marbles = new ArrayList<>();

		int nextMarble = 0;
		marbles.add(nextMarble++);

		int[] score = new int[players];

		int index = 0;
		for (int i = 0; nextMarble <= lastMarble; i = (i + 1) % players, nextMarble++) {
			if (nextMarble % 23 == 0) {
				index = (index - 7 + marbles.size()) % marbles.size();
				score[i] += nextMarble + marbles.remove(index);
			} else {
				index = (index + 2) % marbles.size();
				marbles.add(index, nextMarble);
			}
		}

		return Arrays.stream(score).max().getAsInt();
	}
}

package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Day1 {
	public static void main(String[] args) throws IOException {
		List<String> lines = AdventUtils.readLines("day1.txt");
		System.out.println(getFrequency(lines));
		System.out.println(getFrequencySeenTwice(lines));
	}

	public static int getFrequency(List<String> lines) {
		return lines.stream().mapToInt(Integer::parseInt).sum();
	}

	public static int getFrequencySeenTwice(List<String> lines) {
		int[] changes = lines.stream().mapToInt(Integer::parseInt).toArray();

		int frequency = 0;

		Set<Integer> seenFrequencies = new HashSet<>();
		seenFrequencies.add(frequency);

		int i = 0;
		do {
			frequency += changes[i++ % changes.length];
		} while (seenFrequencies.add(frequency));
		return frequency;
	}
}

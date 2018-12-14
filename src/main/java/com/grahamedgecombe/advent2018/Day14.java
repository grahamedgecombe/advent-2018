package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Day14 {
	public static void main(String[] args) throws IOException {
		String in = AdventUtils.readString("day14.txt");
		System.out.println(getScores(in));
		System.out.println(getRecipes(in));
	}

	public static String getScores(String in) {
		return run(in, true);
	}

	public static String getRecipes(String in) {
		return run(in, false);
	}

	private static String run(String in, boolean part1) {
		int count = Integer.parseInt(in);

		List<Integer> sequence = new ArrayList<>();
		for (char c : in.toCharArray()) {
			sequence.add(c - '0');
		}
		int nextIndexOfAt = 1;

		List<Integer> scores = new ArrayList<>();
		scores.add(3);
		scores.add(7);

		int firstIndex = 0, secondIndex = 1;

		for (;;) {
			if (part1) {
				if (scores.size() >= (count + 10)) {
					break;
				}
			} else {
				if (scores.size() >= nextIndexOfAt) {
					int index = Collections.indexOfSubList(scores, sequence);
					if (index != -1) {
						return Integer.toString(index);
					}
					nextIndexOfAt <<= 1;
				}
			}

			int totalScore = scores.get(firstIndex) + scores.get(secondIndex);

			int oldSize = scores.size();
			do {
				scores.add(totalScore % 10);
				totalScore /= 10;
			} while (totalScore > 0);
			Collections.reverse(scores.subList(oldSize, scores.size()));

			firstIndex = (firstIndex + 1 + scores.get(firstIndex)) % scores.size();
			secondIndex = (secondIndex + 1 + scores.get(secondIndex)) % scores.size();
		}

		StringBuilder buf = new StringBuilder();
		for (int i = count; i < count + 10; i++) {
			buf.append(scores.get(i));
		}
		return buf.toString();
	}
}

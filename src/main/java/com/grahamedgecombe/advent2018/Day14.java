package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Day14 {
	public static void main(String[] args) throws IOException {
		int count = Integer.parseInt(AdventUtils.readString("day14.txt"));
		System.out.println(getScores(count));
	}

	public static String getScores(int count) {
		List<Integer> scores = new ArrayList<>();
		scores.add(3);
		scores.add(7);

		int firstIndex = 0, secondIndex = 1;

		while (scores.size() < (count + 10)) {
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

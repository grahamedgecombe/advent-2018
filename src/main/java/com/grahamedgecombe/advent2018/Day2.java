package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.List;

public final class Day2 {
	public static void main(String[] args) throws IOException {
		List<String> boxes = AdventUtils.readLines("day2.txt");
		System.out.println(getChecksum(boxes));
	}

	private static int[] getFrequencies(String box) {
		int[] frequencies = new int[26];
		for (char c : box.toCharArray()) {
			frequencies[c - 'a']++;
		}
		return frequencies;
	}

	public static int getChecksum(List<String> boxes) {
		int twiceTotal = 0, thriceTotal = 0;
		for (String box : boxes) {
			int[] frequencies = getFrequencies(box);

			boolean twice = false, thrice = false;
			for (int frequency : frequencies) {
				if (frequency == 2) {
					twice = true;
				} else if (frequency == 3) {
					thrice = true;
				}
			}

			if (twice) {
				twiceTotal++;
			}
			if (thrice) {
				thriceTotal++;
			}
		}
		return twiceTotal * thriceTotal;
	}
}

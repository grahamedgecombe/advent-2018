package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.List;

public final class Day2 {
	public static void main(String[] args) throws IOException {
		List<String> boxes = AdventUtils.readLines("day2.txt");
		System.out.println(getChecksum(boxes));
		System.out.println(getCorrectBoxes(boxes));
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

	private static int getDifferentCharIndex(String box1, String box2) {
		int differentIndex = -1;

		for (int i = 0; i < box1.length(); i++) {
			char c1 = box1.charAt(i);
			char c2 = box2.charAt(i);

			if (c1 == c2) {
				continue;
			}

			if (differentIndex != -1) {
				return -1;
			}
			differentIndex = i;
		}

		return differentIndex;
	}

	public static String getCorrectBoxes(List<String> boxes) {
		for (int i = 0; i < boxes.size(); i++) {
			String box1 = boxes.get(i);
			for (int j = 0; j < i; j++) {
				String box2 = boxes.get(j);

				int index = getDifferentCharIndex(box1, box2);
				if (index == -1) {
					continue;
				}

				return box1.substring(0, index) + box1.substring(index + 1);
			}
		}

		throw new IllegalArgumentException();
	}
}

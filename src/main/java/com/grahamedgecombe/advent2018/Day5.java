package com.grahamedgecombe.advent2018;

import java.io.IOException;

public final class Day5 {
	public static void main(String[] args) throws IOException {
		String polymer = AdventUtils.readString("day5.txt");
		System.out.println(react(polymer));
		System.out.println(bestReact(polymer));
	}

	private static char swapCase(char c) {
		if (Character.isUpperCase(c)) {
			return Character.toLowerCase(c);
		} else {
			return Character.toUpperCase(c);
		}
	}

	public static int react(String str) {
		for (int i = 0; i < str.length() - 1;) {
			char c1 = str.charAt(i);
			char c2 = str.charAt(i + 1);
			if (c1 == swapCase(c2)) {
				str = str.substring(0, i).concat(str.substring(i + 2));
				if (i > 0) {
					i--;
				}
			} else {
				i++;
			}
		}
		return str.length();
	}

	private static String removeChars(String str, char ch) {
		StringBuilder builder = new StringBuilder();
		for (char c : str.toCharArray()) {
			if (Character.toLowerCase(c) != ch) {
				builder.append(c);
			}
		}
		return builder.toString();
	}

	public static int bestReact(String str) {
		int len = Integer.MAX_VALUE;
		for (char c = 'a'; c <= 'z'; c++) {
			len = Math.min(len, react(removeChars(str, c)));
		}
		return len;
	}
}

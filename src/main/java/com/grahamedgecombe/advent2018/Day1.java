package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.List;

public final class Day1 {
	public static void main(String[] args) throws IOException {
		List<String> lines = AdventUtils.readLines("day1.txt");
		System.out.println(getFrequency(lines));
	}

	public static int getFrequency(List<String> lines) {
		return lines.stream().mapToInt(Integer::parseInt).sum();
	}
}

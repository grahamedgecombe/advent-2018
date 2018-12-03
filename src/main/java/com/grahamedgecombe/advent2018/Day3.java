package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class Day3 {
	private static final Pattern CLAIM_PATTERN = Pattern.compile("^#([0-9]+) @ ([0-9]+),([0-9]+): ([0-9]+)x([0-9]+)$");

	public static void main(String[] args) throws IOException {
		List<Claim> claims = parseClaims(AdventUtils.readLines("day3.txt"));
		System.out.println(getOverlappingClaims(claims));
	}

	public static final class Claim {
		private final int id, x, y, width, height;

		public Claim(int id, int x, int y, int width, int height) {
			this.id = id;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
	}

	public static List<Claim> parseClaims(List<String> lines) {
		return lines.stream().map(line -> {
			Matcher matcher = CLAIM_PATTERN.matcher(line);
			if (!matcher.matches()) {
				throw new IllegalArgumentException();
			}

			int id = Integer.parseInt(matcher.group(1));
			int x = Integer.parseInt(matcher.group(2));
			int y = Integer.parseInt(matcher.group(3));
			int width = Integer.parseInt(matcher.group(4));
			int height = Integer.parseInt(matcher.group(5));
			return new Claim(id, x, y, width, height);
		}).collect(Collectors.toList());
	}

	public static int getOverlappingClaims(List<Claim> claims) {
		int[][] count = new int[1000][1000];
		int overlappingClaims = 0;

		for (Claim claim : claims) {
			for (int x0 = 0; x0 < claim.width; x0++) {
				for (int y0 = 0; y0 < claim.height; y0++) {
					int c = count[claim.x + x0][claim.y + y0];
					if (c == 1) {
						overlappingClaims++;
					}
					count[claim.x + x0][claim.y + y0] = c + 1;
				}
			}
		}

		return overlappingClaims;
	}
}

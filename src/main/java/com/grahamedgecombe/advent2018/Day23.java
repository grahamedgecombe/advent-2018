package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class Day23 {
	private static final Pattern PATTERN = Pattern.compile("^pos=<([0-9-]+),([0-9-]+),([0-9-]+)>, r=([0-9]+)$");

	public static void main(String[] args) throws IOException {
		List<Nanobot> nanobots = Nanobot.parse(AdventUtils.readLines("day23.txt"));
		System.out.println(getNanobotsInRange(nanobots));
	}

	public static final class Position {
		private final int x, y, z;

		public Position(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public int getDistance(Position other) {
			return Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z);
		}
	}

	public static final class Nanobot {
		public static List<Nanobot> parse(List<String> lines) {
			return lines.stream().map(Nanobot::parse).collect(Collectors.toList());
		}

		public static Nanobot parse(String line) {
			Matcher m = PATTERN.matcher(line);
			if (!m.matches()) {
				throw new IllegalArgumentException();
			}

			int x = Integer.parseInt(m.group(1));
			int y = Integer.parseInt(m.group(2));
			int z = Integer.parseInt(m.group(3));
			int r = Integer.parseInt(m.group(4));

			return new Nanobot(new Position(x, y, z), r);
		}

		private final Position position;
		private final int radius;

		public Nanobot(Position position, int radius) {
			this.position = position;
			this.radius = radius;
		}

		public int getRadius() {
			return radius;
		}

		public boolean contains(Nanobot other) {
			return position.getDistance(other.position) <= radius;
		}
	}

	public static long getNanobotsInRange(List<Nanobot> nanobots) {
		Nanobot nanobot = nanobots.stream().max(Comparator.comparingInt(Nanobot::getRadius)).orElseThrow();
		return nanobots.stream().filter(nanobot::contains).count();
	}
}

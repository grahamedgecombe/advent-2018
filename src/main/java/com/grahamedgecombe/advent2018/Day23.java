package com.grahamedgecombe.advent2018;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
		System.out.println(getDistance(nanobots));
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

	public static String getDistance(List<Nanobot> nanobots) throws IOException {
		Process process = Runtime.getRuntime().exec(new String[] { "z3", "/dev/stdin" });

		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
			writer.write("(declare-const x Int)\n");
			writer.write("(declare-const y Int)\n");
			writer.write("(declare-const z Int)\n");

			writer.write("(define-fun abs ((x Int)) Int (if (> x 0) x (- x)))\n");
			writer.write("(define-fun dist ((x1 Int) (y1 Int) (z1 Int) (x2 Int) (y2 Int) (z2 Int)) Int (+ (abs (- x1 x2)) (abs (- y1 y2)) (abs (- z1 z2))))\n");

			writer.write("(define-fun count ((x Int) (y Int) (z Int)) Int (+\n");
			for (Nanobot nanobot : nanobots) {
				writer.write(String.format("  (if (<= (dist x y z %d %d %d) %d) 1 0)\n", nanobot.position.x, nanobot.position.y, nanobot.position.z, nanobot.radius));
			}
			writer.write("))\n");

			writer.write("(maximize (count x y z))\n");
			writer.write("(minimize (dist x y z 0 0 0))\n");
			writer.write("(check-sat)\n");
			writer.write("(eval (dist x y z 0 0 0))\n");
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			String sat = reader.readLine();
			if (!sat.equals("sat")) {
				throw new IllegalArgumentException();
			}
			return reader.readLine();
		}
	}
}

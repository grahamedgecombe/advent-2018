package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Day10 {
	private static final Pattern POINT_PATTERN = Pattern.compile("^position=<([ 0-9-]+), ([ 0-9-]+)> velocity=<([ 0-9-]+), ([ 0-9-]+)>$");

	public static void main(String[] args) throws IOException {
		Point[] points = parsePoints(AdventUtils.readLines("day10.txt"));
		Result result = getMessage(points);
		System.out.print(result.getMessage());
		System.out.println(result.getTime());
	}

	public static final class Result {
		private final String message;
		private final int time;

		public Result(String message, int time) {
			this.message = message;
			this.time = time;
		}

		public String getMessage() {
			return message;
		}

		public int getTime() {
			return time;
		}
	}

	public static final class Vector {
		private final int x, y;

		public Vector(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Vector add(Vector o) {
			return new Vector(this.x + o.x, this.y + o.y);
		}
	}

	public static final class Point {
		public static Point parse(String line) {
			Matcher matcher = POINT_PATTERN.matcher(line);
			if (!matcher.matches()) {
				System.err.println(line);
				throw new IllegalArgumentException();
			}

			int x = Integer.parseInt(matcher.group(1).trim());
			int y = Integer.parseInt(matcher.group(2).trim());
			int velocityX = Integer.parseInt(matcher.group(3).trim());
			int velocityY = Integer.parseInt(matcher.group(4).trim());
			return new Point(new Vector(x, y), new Vector(velocityX, velocityY));
		}

		private final Vector position;
		private final Vector velocity;

		public Point(Vector position, Vector velocity) {
			this.position = position;
			this.velocity = velocity;
		}

		public Point tick() {
			return new Point(position.add(velocity), velocity);
		}
	}

	public static Point[] parsePoints(List<String> lines) {
		return lines.stream().map(Point::parse).toArray(Point[]::new);
	}

	private static long getBoundingBoxArea(Point[] points) {
		IntSummaryStatistics xStats = Arrays.stream(points).mapToInt(p -> p.position.x).summaryStatistics();
		IntSummaryStatistics yStats = Arrays.stream(points).mapToInt(p -> p.position.y).summaryStatistics();
		return ((long) (xStats.getMax() - xStats.getMin())) * (yStats.getMax() - yStats.getMin());
	}

	private static String render(Point[] points) {
		IntSummaryStatistics xStats = Arrays.stream(points).mapToInt(p -> p.position.x).summaryStatistics();
		IntSummaryStatistics yStats = Arrays.stream(points).mapToInt(p -> p.position.y).summaryStatistics();

		int width = xStats.getMax() - xStats.getMin() + 1;
		int height = yStats.getMax() - yStats.getMin() + 1;

		char[][] grid = new char[height][width];
		for (int y = 0; y < height; y++) {
			Arrays.fill(grid[y], '.');
		}

		for (Point point : points) {
			int x = point.position.x - xStats.getMin();
			int y = point.position.y - yStats.getMin();
			grid[y][x] = '#';
		}

		StringBuilder str = new StringBuilder();
		for (int y = 0; y < height; y++) {
			str.append(grid[y]);
			str.append("\n");
		}
		return str.toString();
	}

	public static Result getMessage(Point[] points) {
		for (int time = 0;; time++) {
			long area = getBoundingBoxArea(points);

			Point[] next = Arrays.stream(points).map(Point::tick).toArray(Point[]::new);
			long nextArea = getBoundingBoxArea(next);

			if (nextArea > area) {
				return new Result(render(points), time);
			}

			points = next;
		}
	}
}

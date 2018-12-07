package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;

public final class Day6 {
	public static void main(String[] args) throws IOException {
		Point[] points = parsePoints(AdventUtils.readLines("day6.txt"));
		Result result = getAreas(points, 10000);
		System.out.println(result.getMaxArea());
		System.out.println(result.getSafeArea());
	}

	public static Point[] parsePoints(List<String> lines) {
		return lines.stream().map(Point::parse).toArray(Point[]::new);
	}

	public static final class Point {
		public static Point parse(String line) {
			String[] parts = line.split(", ");
			if (parts.length != 2) {
				throw new IllegalArgumentException();
			}
			return new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
		}

		private final int x, y;
		private boolean infinite = false;
		private int area = 0;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getArea() {
			return area;
		}

		public int getDistance(int otherX, int otherY) {
			return Math.abs(x - otherX) + Math.abs(y - otherY);
		}

		@Override
		public String toString() {
			return "(" + x + ", " + y + ")";
		}
	}

	public static final class Result {
		private final int maxArea, safeArea;

		public Result(int maxArea, int safeArea) {
			this.maxArea = maxArea;
			this.safeArea = safeArea;
		}

		public int getMaxArea() {
			return maxArea;
		}

		public int getSafeArea() {
			return safeArea;
		}
	}

	public static Result getAreas(Point[] points, int totalThreshold) {
		IntSummaryStatistics xStats = Arrays.stream(points).mapToInt(Point::getX).summaryStatistics();
		IntSummaryStatistics yStats = Arrays.stream(points).mapToInt(Point::getY).summaryStatistics();

		int minX = xStats.getMin();
		int minY = yStats.getMin();

		int maxX = xStats.getMax();
		int maxY = yStats.getMax();

		int safeArea = 0;

		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				final int fX = x;
				final int fY = y;

				int totalDistance = Arrays.stream(points)
					.mapToInt(p -> p.getDistance(fX, fY))
					.sum();
				if (totalDistance < totalThreshold) {
					safeArea++;
				}

				Point[] nearestTwo = Arrays.stream(points)
					.sorted(Comparator.comparingInt(p -> p.getDistance(fX, fY)))
					.limit(2)
					.toArray(Point[]::new);

				/* at least two equally likely, skip */
				Point nearest = nearestTwo[0];
				if (nearest.getDistance(fX, fY) == nearestTwo[1].getDistance(fX, fY)) {
					continue;
				}

				if (x == minX || x == maxX || y == minY || y == maxY) {
					nearest.infinite = true;
					continue;
				}

				nearest.area++;
			}
		}

		int maxArea = Arrays.stream(points)
			.filter(p -> !p.infinite)
			.mapToInt(Point::getArea)
			.max()
			.getAsInt();

		return new Result(maxArea, safeArea);
	}
}

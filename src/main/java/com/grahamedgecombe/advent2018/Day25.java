package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class Day25 {
	public static void main(String[] args) throws IOException {
		System.out.println(getConstellations(AdventUtils.readLines("day25.txt")));
	}

	public static final class Position {
		public static List<Position> parse(List<String> lines) {
			return lines.stream().map(Position::parse).collect(Collectors.toList());
		}

		public static Position parse(String line) {
			int[] coords = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
			return new Position(coords[0], coords[1], coords[2], coords[3]);
		}

		private final int w, x, y, z;

		public Position(int w, int x, int y, int z) {
			this.w = w;
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public int getDistance(Position other) {
			return Math.abs(w - other.w) + Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			Position position = (Position) o;
			return w == position.w &&
				x == position.x &&
				y == position.y &&
				z == position.z;
		}

		@Override
		public int hashCode() {
			return Objects.hash(w, x, y, z);
		}

		@Override
		public String toString() {
			return "(" + w + ", " + x + ", " + y + ", " + z + ")";
		}
	}

	public static int getConstellations(List<String> lines) {
		List<Position> positions = Position.parse(lines);
		DisjointSet<Position> disjointSet = new ForestDisjointSet<>();

		for (int i = 0; i < positions.size(); i++) {
			Position v1 = positions.get(i);
			DisjointSet.Partition<Position> p1 = disjointSet.add(v1);

			for (int j = 0; j < i; j++) {
				Position v2 = positions.get(j);
				DisjointSet.Partition<Position> p2 = disjointSet.get(v2);

				if (v1.getDistance(v2) <= 3) {
					disjointSet.union(p1, p2);
				}
			}
		}

		return disjointSet.partitions();
	}
}

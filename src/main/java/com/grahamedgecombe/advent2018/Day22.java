package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Day22 {
	private static final Pattern PATTERN = Pattern.compile("^depth: ([0-9]+)\ntarget: ([0-9]+),([0-9]+)$", Pattern.MULTILINE);

	public static void main(String[] args) throws IOException {
		Matcher m = PATTERN.matcher(AdventUtils.readString("day22.txt"));
		if (!m.matches()) {
			throw new IllegalArgumentException();
		}

		int depth = Integer.parseInt(m.group(1));
		int targetX = Integer.parseInt(m.group(2));
		int targetY = Integer.parseInt(m.group(3));

		Grid grid = new Grid(depth, targetX, targetY);
		System.out.println(grid.sumRisk());
	}

	public static final class Position {
		private final int x, y;

		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Position add(int dx, int dy) {
			return new Position(x + dx, y + dy);
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
			return x == position.x &&
				y == position.y;
		}

		@Override
		public int hashCode() {
			return Objects.hash(x, y);
		}
	}

	public static final class Grid {
		private final Map<Position, Integer> geologicalIndexes = new HashMap<>();
		private final int depth, targetX, targetY;

		public Grid(int depth, int targetX, int targetY) {
			this.depth = depth;
			this.targetX = targetX;
			this.targetY = targetY;
		}

		public int getGeologicalIndex(Position position) {
			Integer geologicalIndex = geologicalIndexes.get(position);
			if (geologicalIndex != null) {
				return geologicalIndex;
			}

			if ((position.x == 0 && position.y == 0) || (position.x == targetX && position.y == targetY)) {
				return 0;
			} else if (position.y == 0) {
				return position.x * 16807;
			} else if (position.x == 0) {
				return position.y * 48271;
			}

			geologicalIndex = getErosionLevel(position.add(-1, 0)) * getErosionLevel(position.add(0, -1));
			geologicalIndexes.put(position, geologicalIndex);
			return geologicalIndex;
		}

		public int getErosionLevel(Position position) {
			return (getGeologicalIndex(position) + depth) % 20183;
		}

		public int getRisk(Position position) {
			return getErosionLevel(position) % 3;
		}

		public int sumRisk() {
			int risk = 0;
			for (int x = 0; x <= targetX; x++) {
				for (int y = 0; y <= targetY; y++) {
					risk += getRisk(new Position(x, y));
				}
			}
			return risk;
		}
	}
}

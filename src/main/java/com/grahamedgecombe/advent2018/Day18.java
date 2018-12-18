package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Day18 {
	public static void main(String[] args) throws IOException {
		Grid grid = Grid.parse(AdventUtils.readLines("day18.txt"));
		System.out.println(grid.getResourceValue(10));
		System.out.println(grid.getResourceValue(1000000000));
	}

	private static final class State {
		private final char[][] tiles;

		public State(char[][] tiles) {
			this.tiles = tiles;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			State state = (State) o;
			return Arrays.deepEquals(tiles, state.tiles);
		}

		@Override
		public int hashCode() {
			return Arrays.deepHashCode(tiles);
		}
	}

	public static final class Grid {
		public static Grid parse(List<String> lines) {
			int height = lines.size();
			int width = lines.get(0).length();
			char[][] tiles = new char[height][];

			for (int y = 0; y < height; y++) {
				String line = lines.get(y);
				if (line.length() != width) {
					throw new IllegalArgumentException();
				}
				tiles[y] = line.toCharArray();
			}

			return new Grid(width, height, tiles);
		}

		private final int width, height;
		private final char[][] tiles;

		public Grid(int width, int height, char[][] tiles) {
			this.width = width;
			this.height = height;
			this.tiles = tiles;
		}

		public int getResourceValue(long minutes) {
			Map<State, Long> history = new HashMap<>();

			char[][] tiles = this.tiles;
			history.put(new State(tiles), 0L);

			for (long i = 1; i <= minutes; i++) {
				tiles = tick(tiles);

				State state = new State(tiles);
				if (history.containsKey(state)) {
					long cycleLength = i - history.get(state);
					long remaining = minutes - i;
					i = minutes - (remaining % cycleLength);
				} else {
					history.put(state, i);
				}
			}

			int trees = 0, lumberyards = 0;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					char tile = tiles[y][x];
					if (tile == '|') {
						trees++;
					} else if (tile == '#') {
						lumberyards++;
					}
				}
			}
			return trees * lumberyards;
		}

		private char[][] tick(char[][] tiles) {
			char[][] nextTiles = new char[height][width];

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int trees = 0, lumberyards = 0;

					for (int dy = -1; dy <= 1; dy++) {
						for (int dx = -1; dx <= 1; dx++) {
							if (dy == 0 && dx == 0) {
								continue;
							}

							int y0 = y + dy;
							int x0 = x + dx;
							if (y0 < 0 || y0 >= height || x0 < 0 || x0 >= width) {
								continue;
							}

							char tile = tiles[y0][x0];
							if (tile == '|') {
								trees++;
							} else if (tile == '#') {
								lumberyards++;
							}
						}
					}

					char tile = tiles[y][x];
					char nextTile = tile;
					if (tile == '.' && trees >= 3) {
						nextTile = '|';
					} else if (tile == '|' && lumberyards >= 3) {
						nextTile = '#';
					} else if (tile == '#') {
						nextTile = trees >= 1 && lumberyards >= 1 ? '#' : '.';
					}

					nextTiles[y][x] = nextTile;
				}
			}

			return nextTiles;
		}

		@Override
		public String toString() {
			StringBuilder buf = new StringBuilder();
			for (int y = 0; y < height; y++) {
				buf.append(tiles[y], 0, width);
				buf.append('\n');
			}
			return buf.toString();
		}
	}
}

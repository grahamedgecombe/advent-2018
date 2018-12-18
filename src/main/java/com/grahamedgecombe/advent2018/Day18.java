package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.List;

public final class Day18 {
	public static void main(String[] args) throws IOException {
		Grid grid = Grid.parse(AdventUtils.readLines("day18.txt"));
		System.out.println(grid.getResourceValue(10));
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
		private char[][] tiles, nextTiles;

		public Grid(int width, int height, char[][] tiles) {
			this.width = width;
			this.height = height;
			this.tiles = tiles;
			this.nextTiles = new char[height][width];
		}

		public int getResourceValue(int minutes) {
			for (int i = 0; i < minutes; i++) {
				tick();
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

		private void tick() {
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

			char[][] temp = tiles;
			tiles = nextTiles;
			nextTiles = temp;
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

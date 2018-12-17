package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class Day17 {
	private static final Pattern PATTERN = Pattern.compile("^([xy])=([0-9]+), [xy]=([0-9]+)..([0-9]+)$");
	private static final int SPRING_X = 500, SPRING_Y = 0;

	public static void main(String[] args) throws IOException {
		Grid grid = Grid.parse(AdventUtils.readLines("day17.txt"));
		grid.flow();
		System.out.println(grid.getWaterTiles());
		System.out.println(grid.getStillWaterTiles());
	}

	private static final class Vein {
		public static Vein parse(String line) {
			Matcher m = PATTERN.matcher(line);
			if (!m.matches()) {
				throw new IllegalArgumentException();
			}
			int minX, maxX, minY, maxY;
			if (m.group(1).equals("x")) {
				minX = maxX = Integer.parseInt(m.group(2));
				minY = Integer.parseInt(m.group(3));
				maxY = Integer.parseInt(m.group(4));
			} else {
				minY = maxY = Integer.parseInt(m.group(2));
				minX = Integer.parseInt(m.group(3));
				maxX = Integer.parseInt(m.group(4));
			}
			return new Vein(minX, maxX, minY, maxY);
		}

		private final int minX, maxX, minY, maxY;

		public Vein(int minX, int maxX, int minY, int maxY) {
			this.minX = minX;
			this.maxX = maxX;
			this.minY = minY;
			this.maxY = maxY;
		}

		public int getMinX() {
			return minX;
		}

		public int getMaxX() {
			return maxX;
		}

		public int getMinY() {
			return minY;
		}

		public int getMaxY() {
			return maxY;
		}
	}

	public static final class Grid {
		public static Grid parse(List<String> lines) {
			List<Vein> veins = lines.stream().map(Vein::parse).collect(Collectors.toList());

			int minX = veins.stream().mapToInt(Vein::getMinX).min().getAsInt() - 1;
			int maxX = veins.stream().mapToInt(Vein::getMaxX).max().getAsInt() + 1;
			int minY = veins.stream().mapToInt(Vein::getMinY).min().getAsInt();
			int maxY = veins.stream().mapToInt(Vein::getMaxY).max().getAsInt();

			int offX = minX;
			int offY = Math.min(minY, SPRING_Y);
			minY -= offY;

			int width = (maxX - offX) + 1;
			int height = (maxY - offY) + 1;

			char[][] tiles = new char[height][width];
			for (int y = 0; y < height; y++) {
				Arrays.fill(tiles[y], '.');
			}

			for (Vein vein : veins) {
				for (int y = vein.minY - offY; y <= vein.maxY - offY; y++) {
					for (int x = vein.minX - offX; x <= vein.maxX - offX; x++) {
						tiles[y][x] = '#';
					}
				}
			}

			return new Grid(offX, offY, minY, width, height, tiles);
		}

		private final int offX, offY, minY, width, height;
		private final char[][] tiles;

		public Grid(int offX, int offY, int minY, int width, int height, char[][] tiles) {
			this.offX = offX;
			this.offY = offY;
			this.minY = minY;
			this.width = width;
			this.height = height;
			this.tiles = tiles;
		}

		public void flow() {
			int x = SPRING_X - offX;
			int y = SPRING_Y - offY;
			tiles[y][x] = '+';
			flowDown(x, y);
		}

		public int getWaterTiles() {
			int count = 0;
			for (int y = minY; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (tiles[y][x] == '|' || tiles[y][x] == '~') {
						count++;
					}
				}
			}
			return count;
		}

		public int getStillWaterTiles() {
			int count = 0;
			for (int y = minY; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (tiles[y][x] == '~') {
						count++;
					}
				}
			}
			return count;
		}

		private void flowDown(int x, int y) {
			if (y >= height) {
				return;
			} else if (tiles[y][x] == '.') {
				tiles[y][x] = '|';
			} else if (tiles[y][x] != '+') {
				return;
			}

			if ((y + 1) >= height) {
				return;
			}

			flowDown(x, y + 1);

			if (tiles[y + 1][x] == '#' || tiles[y + 1][x] == '~') {
				int leftX = flowLeft(x - 1, y);
				int rightX = flowRight(x + 1, y);

				if (leftX != -1 && rightX != -1) {
					for (int x0 = leftX + 1; x0 < rightX; x0++) {
						tiles[y][x0] = '~';
					}
				}
			}
		}

		private int flowLeft(int x, int y) {
			if (x < 0) {
				return -1;
			} else if (tiles[y][x] == '.') {
				tiles[y][x] = '|';
			} else if (tiles[y][x] == '#') {
				return x;
			}

			flowDown(x, y + 1);

			if (tiles[y + 1][x] == '#' || tiles[y + 1][x] == '~') {
				return flowLeft(x - 1, y);
			}

			return -1;
		}

		private int flowRight(int x, int y) {
			if (x >= width) {
				return -1;
			} else if (tiles[y][x] == '.') {
				tiles[y][x] = '|';
			} else if (tiles[y][x] == '#') {
				return x;
			}

			flowDown(x, y + 1);

			if (tiles[y + 1][x] == '#' || tiles[y + 1][x] == '~') {
				return flowRight(x + 1, y);
			}

			return -1;
		}

		@Override
		public String toString() {
			StringBuilder buf = new StringBuilder();
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					buf.append(tiles[y][x]);
				}
				buf.append("\n");
			}
			return buf.toString();
		}
	}
}

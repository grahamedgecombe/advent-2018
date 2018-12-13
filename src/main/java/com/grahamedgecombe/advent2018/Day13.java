package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Preconditions;

public final class Day13 {
	public static void main(String[] args) throws IOException {
		Grid grid = Grid.parse(AdventUtils.readLines("day13.txt"));
		System.out.println(grid.getCrashLocation());
	}

	private enum Direction {
		UP(0, -1, '^'),
		RIGHT(1, 0, '>'),
		DOWN(0, 1, 'v'),
		LEFT(-1, 0, '<');

		public static Direction fromChar(char c) {
			return Arrays.stream(values()).filter(v -> v.ch == c).findFirst().orElseThrow(IllegalArgumentException::new);
		}

		private final int deltaX, deltaY;
		private final char ch;

		Direction(int deltaX, int deltaY, char ch) {
			this.deltaX = deltaX;
			this.deltaY = deltaY;
			this.ch = ch;
		}

		public Direction left() {
			Direction[] values = Direction.values();
			return values[(ordinal() + values.length - 1) % values.length];
		}

		public Direction right() {
			Direction[] values = Direction.values();
			return values[(ordinal() + 1) % values.length];
		}
	}

	private static final class Cart {
		private Direction direction;
		private int intersections, lastTick;

		public Cart(Direction direction) {
			this.direction = direction;
		}

		public Direction nextDirection(char track) {
			if (track == '+') {
				switch (intersections++ % 3) {
					case 0:
						return direction.left();
					case 1:
						return direction;
					default:
						return direction.right();
				}
			} else if (track == '-') {
				Preconditions.checkState(direction == Direction.LEFT || direction == Direction.RIGHT);
				return direction;
			} else if (track == '|') {
				Preconditions.checkState(direction == Direction.UP || direction == Direction.DOWN);
				return direction;
			} else if (track == '/') {
				switch (direction) {
					case UP:
						return Direction.RIGHT;
					case RIGHT:
						return Direction.UP;
					case DOWN:
						return Direction.LEFT;
					default:
						return Direction.DOWN;
				}
			} else if (track == '\\') {
				switch (direction) {
					case UP:
						return Direction.LEFT;
					case RIGHT:
						return Direction.DOWN;
					case DOWN:
						return Direction.RIGHT;
					default:
						return Direction.UP;
				}
			} else {
				throw new IllegalStateException();
			}
		}
	}

	public static final class Grid {
		public static Grid parse(List<String> lines) {
			int width = lines.stream().mapToInt(String::length).max().orElseThrow(IllegalArgumentException::new);
			int height = lines.size();

			char[][] tracks = new char[height][width];
			Cart[][] carts = new Cart[height][width];

			for (int y = 0; y < height; y++) {
				String line = lines.get(y);
				for (int x = 0; x < line.length(); x++) {
					char c = line.charAt(x);
					if (c == '^' || c == 'v') {
						tracks[y][x] = '|';
						carts[y][x] = new Cart(Direction.fromChar(c));
					} else if (c == '<' || c == '>') {
						tracks[y][x] = '-';
						carts[y][x] = new Cart(Direction.fromChar(c));
					} else {
						tracks[y][x] = c;
					}
				}
			}

			return new Grid(width, height, tracks, carts);
		}

		private final int width, height;
		private final char[][] tracks;
		private final Cart[][] carts;
		private int tick;

		private Grid(int width, int height, char[][] tracks, Cart[][] carts) {
			this.width = width;
			this.height = height;
			this.tracks = tracks;
			this.carts = carts;
		}

		private String tick() {
			tick++;

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					Cart cart = carts[y][x];
					if (cart == null || cart.lastTick == tick) {
						continue;
					}

					char track = tracks[y][x];

					Direction direction = cart.nextDirection(track);
					cart.direction = direction;
					cart.lastTick = tick;

					int x0 = x + direction.deltaX;
					int y0 = y + direction.deltaY;
					if (carts[y0][x0] != null) {
						return x0 + "," + y0;
					}

					carts[y][x] = null;
					carts[y0][x0] = cart;
				}
			}

			return null;
		}

		public String getCrashLocation() {
			String location;
			while ((location = tick()) == null);
			return location;
		}
	}
}

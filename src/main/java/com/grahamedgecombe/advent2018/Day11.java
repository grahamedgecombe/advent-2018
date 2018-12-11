package com.grahamedgecombe.advent2018;

import java.io.IOException;

public final class Day11 {
	private static final int GRID_SIZE = 300;

	public static void main(String[] args) throws IOException {
		int serial = Integer.parseInt(AdventUtils.readString("day11.txt"));
		System.out.println(getMostPowerful3x3Square(serial));
		System.out.println(getMostPowerfulSquare(serial));
	}

	public static int getPower(int x, int y, int serial) {
		int rackId = x + 10;
		return (((rackId * y + serial) * rackId) / 100) % 10 - 5;
	}

	public static String getMostPowerful3x3Square(int serial) {
		final int size = 3;

		String square = null;
		int maxPower = Integer.MIN_VALUE;

		for (int x = 0; x <= GRID_SIZE - size; x++) {
			for (int y = 0; y <= GRID_SIZE - size; y++) {
				int power = 0;
				for (int x0 = x; x0 < x + size; x0++) {
					for (int y0 = y; y0 < y + size; y0++) {
						power += getPower(x0, y0, serial);
					}
				}

				if (power > maxPower) {
					square = x + "," + y;
					maxPower = power;
				}
			}
		}

		return square;
	}

	public static String getMostPowerfulSquare(int serial) {
		int[][] powers = new int[GRID_SIZE][GRID_SIZE];

		String square = null;
		int maxPower = Integer.MIN_VALUE;

		for (int x = 0; x < GRID_SIZE; x++) {
			for (int y = 0; y < GRID_SIZE; y++) {
				int power = getPower(x, y, serial);
				if (x > 0) {
					power += powers[x - 1][y];
				}
				if (y > 0) {
					power += powers[x][y - 1];
				}
				if (x > 0 && y > 0) {
					power -= powers[x - 1][y - 1];
				}
				powers[x][y] = power;
			}
		}

		for (int size = 1; size <= GRID_SIZE; size++) {
			for (int x = 0; x <= GRID_SIZE - size; x++) {
				for (int y = 0; y <= GRID_SIZE - size; y++) {
					int power = powers[x + size - 1][y + size - 1];
					if (x > 0 && y > 0) {
						power += powers[x - 1][y - 1];
					}
					if (y > 0) {
						power -= powers[x + size - 1][y - 1];
					}
					if (x > 0) {
						power -= powers[x - 1][y + size - 1];
					}

					if (power > maxPower) {
						square = x + "," + y + "," + size;
						maxPower = power;
					}
				}
			}
		}

		return square;
	}
}

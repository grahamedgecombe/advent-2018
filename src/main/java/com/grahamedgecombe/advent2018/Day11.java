package com.grahamedgecombe.advent2018;

import java.io.IOException;

public final class Day11 {
	private static final int GRID_SIZE = 300;

	public static void main(String[] args) throws IOException {
		int serial = Integer.parseInt(AdventUtils.readString("day11.txt"));
		System.out.println(getMostPowerful3x3Square(serial));
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
}

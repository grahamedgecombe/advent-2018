package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Day4 {
	private static final Pattern EVENT_PATTERN = Pattern.compile("^\\[([0-9]{4})-([0-9]{2})-([0-9]{2}) ([0-9]{2}):([0-9]{2})] (wakes up|falls asleep|Guard #([0-9]+) begins shift)$");

	public static void main(String[] args) throws IOException {
		Map<Integer, Guard> guards = parseGuards(AdventUtils.readLines("day4.txt"));
		System.out.println(getSleepiestGuard(guards));
		System.out.println(getSleepiestMinute(guards));
	}

	private static final class Guard {
		private final int id;
		private final int[] minutes = new int[60];
		private int totalMinutes = 0;

		public Guard(int id) {
			this.id = id;
		}

		public void addMinutes(int start, int end) {
			for (int i = start; i < end; i++) {
				minutes[i]++;
				totalMinutes++;
			}
		}

		public int getTotalMinutes() {
			return totalMinutes;
		}

		public int getSleepiestMinute() {
			int maxKey = 0;
			int maxValue = minutes[0];

			for (int i = 1; i < minutes.length; i++) {
				int value = minutes[i];
				if (value > maxValue) {
					maxKey = i;
					maxValue = value;
				}
			}

			return maxKey;
		}

		public int getSleepiestMinuteFrequency() {
			int sleepiestMinute = getSleepiestMinute();
			return minutes[sleepiestMinute];
		}
	}

	public static Map<Integer, Guard> parseGuards(List<String> lines) {
		Map<Integer, Guard> guards = new HashMap<>();
		int guardId = 0, fellAsleepAt = 0;

		Collections.sort(lines);
		for (String line : lines) {
			Matcher matcher = EVENT_PATTERN.matcher(line);
			if (!matcher.matches()) {
				throw new IllegalStateException();
			}

			int minute = Integer.parseInt(matcher.group(5));

			String event = matcher.group(6);
			if (event.equals("wakes up")) {
				Guard guard = guards.get(guardId);
				if (guard == null) {
					guard = new Guard(guardId);
					guards.put(guardId, guard);
				}
				guard.addMinutes(fellAsleepAt, minute);
			} else if (event.equals("falls asleep")) {
				fellAsleepAt = minute;
			} else {
				guardId = Integer.parseInt(matcher.group(7));
			}
		}

		return guards;
	}

	public static int getSleepiestGuard(Map<Integer, Guard> guards) {
		Guard guard = guards.values()
			.stream()
			.max(Comparator.comparing(Guard::getTotalMinutes))
			.get();
		return guard.id * guard.getSleepiestMinute();
	}

	public static int getSleepiestMinute(Map<Integer, Guard> guards) {
		Guard guard = guards.values()
			.stream()
			.max(Comparator.comparing(Guard::getSleepiestMinuteFrequency))
			.get();
		return guard.id * guard.getSleepiestMinute();
	}
}

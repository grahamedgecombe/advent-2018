package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Day12 {
	private static final Pattern INITIAL_STATE_PATTERN = Pattern.compile("^initial state: ([#.]+)$");
	private static final Pattern RULE_PATTERN = Pattern.compile("^([#.]{5}) => ([#.])$");
	private static final int RULE_LENGTH = 5;

	public static void main(String[] args) throws IOException {
		Pots pots = Pots.parse(AdventUtils.readLines("day12.txt"));
		System.out.println(pots.getSum(20));
		System.out.println(pots.getSum(50_000_000_000L));
	}

	private static final class State {
		private static final State EMPTY = new State(0, "");

		private final long zeroIndex;
		private final String chars;

		private State(long zeroIndex, String chars) {
			this.zeroIndex = zeroIndex;
			this.chars = chars;
		}
	}

	public static final class Pots {
		public static Pots parse(List<String> lines) {
			Map<String, Character> rules = new HashMap<>();
			String initialState = null;

			for (String line : lines) {
				Matcher matcher = INITIAL_STATE_PATTERN.matcher(line);
				if (matcher.matches()) {
					initialState = matcher.group(1);
					continue;
				}

				matcher = RULE_PATTERN.matcher(line);
				if (matcher.matches()) {
					rules.put(matcher.group(1), matcher.group(2).charAt(0));
					continue;
				}

				if (!line.isEmpty()) {
					throw new IllegalArgumentException();
				}
			}

			return new Pots(rules, initialState);
		}

		private final Map<String, Character> rules;
		private final String initialState;

		private Pots(Map<String, Character> rules, String initialState) {
			this.rules = rules;
			this.initialState = initialState;
		}

		private State generate(State current) {
			StringBuilder next = new StringBuilder();

			for (int j = -(RULE_LENGTH - 1); j < current.chars.length(); j++) {
				char[] rule = new char[RULE_LENGTH];
				for (int k = 0; k < rule.length; k++) {
					int l = j + k;
					if (l >= 0 && l < current.chars.length()) {
						rule[k] = current.chars.charAt(l);
					} else {
						rule[k] = '.';
					}
				}
				next.append(rules.getOrDefault(new String(rule), '.'));
			}

			int firstIndex = next.indexOf("#");
			int lastIndex = next.lastIndexOf("#");

			if (firstIndex != -1) {
				long zeroIndex = current.zeroIndex + ((RULE_LENGTH - 1) / 2) - firstIndex;
				return new State(zeroIndex, next.substring(firstIndex, lastIndex + 1));
			} else {
				return State.EMPTY;
			}
		}

		public long getSum(long n) {
			Map<String, Long> history = new HashMap<>();
			Map<String, Long> zeroIndexes = new HashMap<>();

			State state = new State(0, initialState);
			for (long i = 0; i < n; i++) {
				state = generate(state);

				if (history.containsKey(state.chars)) {
					long cycleLength = i - history.get(state.chars);
					long zeroIndexDelta = state.zeroIndex - zeroIndexes.get(state.chars);

					long remaining = n - i - 1;

					long cycles = remaining / cycleLength;
					long zeroIndex = state.zeroIndex + cycles * zeroIndexDelta;

					state = new State(zeroIndex, state.chars);
					i = n - (remaining % cycleLength);
				} else {
					history.put(state.chars, i);
					zeroIndexes.put(state.chars, state.zeroIndex);
				}
			}

			long sum = 0;
			for (int i = 0; i < state.chars.length(); i++) {
				if (state.chars.charAt(i) == '#') {
					sum += i - state.zeroIndex;
				}
			}
			return sum;
		}
	}
}

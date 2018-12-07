package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class Day7 {
	private static final Pattern DEPENDENCY = Pattern.compile("^Step ([A-Z]) must be finished before step ([A-Z]) can begin\\.$");

	public static void main(String[] args) throws IOException {
		List<Step> steps = parseSteps(AdventUtils.readLines("day7.txt"));
		System.out.println(getOrder(steps));
	}

	public static final class Step implements Comparable<Step> {
		private final char id;
		private final List<Step> dependencies = new ArrayList<>();
		private boolean visited = false;

		public Step(char id) {
			this.id = id;
		}

		@Override
		public int compareTo(Step o) {
			return id - o.id;
		}

		public boolean isVisited() {
			return visited;
		}
	}

	public static List<Step> parseSteps(List<String> lines) {
		Map<Character, Step> nodes = new HashMap<>();

		for (String line : lines) {
			Matcher matcher = DEPENDENCY.matcher(line);
			if (!matcher.matches()) {
				throw new IllegalArgumentException();
			}

			char id = matcher.group(2).charAt(0);
			char dependencyId = matcher.group(1).charAt(0);

			Step step = nodes.computeIfAbsent(id, Step::new);
			Step dependency = nodes.computeIfAbsent(dependencyId, Step::new);
			step.dependencies.add(dependency);
		}

		return nodes.values().stream().sorted().collect(Collectors.toList());
	}

	public static String getOrder(List<Step> steps) {
		StringBuilder order = new StringBuilder();

		while (!steps.stream().allMatch(Step::isVisited)) {
			for (Step step : steps) {
				if (step.visited) {
					continue;
				}

				if (!step.dependencies.stream().allMatch(Step::isVisited)) {
					continue;
				}

				step.visited = true;
				order.append(step.id);
				break;
			}
		}

		return order.toString();
	}
}

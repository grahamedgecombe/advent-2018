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
		System.out.println(getDuration(steps, 5, 60));
	}

	public enum State {
		AVAILABLE,
		IN_PROGRESS,
		DONE
	}

	public static final class Step implements Comparable<Step> {
		private final char id;
		private final List<Step> dependencies = new ArrayList<>();
		private State state = State.AVAILABLE;

		public Step(char id) {
			this.id = id;
		}

		@Override
		public int compareTo(Step o) {
			return id - o.id;
		}

		public void reset() {
			state = State.AVAILABLE;
		}

		public boolean isDone() {
			return state == State.DONE;
		}

		public int getDuration(int extraDuration) {
			return id - 'A' + 1 + extraDuration;
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
		steps.forEach(Step::reset);

		StringBuilder order = new StringBuilder();

		while (!steps.stream().allMatch(Step::isDone)) {
			for (Step step : steps) {
				if (step.state != State.AVAILABLE) {
					continue;
				}

				if (!step.dependencies.stream().allMatch(Step::isDone)) {
					continue;
				}

				step.state = State.DONE;
				order.append(step.id);
				break;
			}
		}

		return order.toString();
	}

	private static final class Worker {
		private Step step;
		private int finishAt;
	}

	public static int getDuration(List<Step> steps, int workerCount, int extraDuration) {
		steps.forEach(Step::reset);

		Worker[] workers = new Worker[workerCount];
		for (int i = 0; i < workers.length; i++) {
			workers[i] = new Worker();
		}

		int time = 0;

		while (!steps.stream().allMatch(Step::isDone)) {
			for (Worker worker : workers) {
				if (worker.step != null && worker.finishAt == time) {
					worker.step.state = State.DONE;
					worker.step = null;
				}
			}

			for (Worker worker : workers) {
				if (worker.step != null) {
					continue;
				}

				for (Step step : steps) {
					if (step.state != State.AVAILABLE) {
						continue;
					}

					if (!step.dependencies.stream().allMatch(Step::isDone)) {
						continue;
					}

					step.state = State.IN_PROGRESS;
					worker.step = step;
					worker.finishAt = time + step.getDuration(extraDuration);
					break;
				}
			}

			time++;
		}

		return time - 1;
	}
}

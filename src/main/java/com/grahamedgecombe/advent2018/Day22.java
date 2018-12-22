package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Day22 {
	private static final Pattern PATTERN = Pattern.compile("^depth: ([0-9]+)\ntarget: ([0-9]+),([0-9]+)$", Pattern.MULTILINE);

	public static void main(String[] args) throws IOException {
		Matcher m = PATTERN.matcher(AdventUtils.readString("day22.txt"));
		if (!m.matches()) {
			throw new IllegalArgumentException();
		}

		int depth = Integer.parseInt(m.group(1));
		int targetX = Integer.parseInt(m.group(2));
		int targetY = Integer.parseInt(m.group(3));

		Grid grid = new Grid(depth, targetX, targetY);
		System.out.println(grid.sumRisk());
		System.out.println(grid.getMinutes());
	}

	public static final class Position {
		private final int x, y;

		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Position add(int dx, int dy) {
			return new Position(x + dx, y + dy);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			Position position = (Position) o;
			return x == position.x &&
				y == position.y;
		}

		@Override
		public int hashCode() {
			return Objects.hash(x, y);
		}
	}

	private static final int ROCKY = 0;
	private static final int WET = 1;
	private static final int NARROW = 2;

	private enum Tool {
		TORCH(ROCKY, NARROW),
		CLIMBING_GEAR(ROCKY, WET),
		NEITHER(WET, NARROW);

		private final List<Integer> usableRisks;

		Tool(Integer... usableRisks) {
			this.usableRisks = Arrays.asList(usableRisks);
		}
	}

	public static final class Grid {
		private final class State extends AStar.Node<State> {
			private final Position position;
			private final Tool tool;

			public State(Position position, Tool tool) {
				this.position = position;
				this.tool = tool;
			}

			@Override
			public boolean isGoal() {
				return position.x == targetX && position.y == targetY && tool == Tool.TORCH;
			}

			private void addNeighbour(List<State> neighbours, int dx, int dy) {
				Position nextPos = position.add(dx, dy);
				if (nextPos.x < 0 || nextPos.y < 0) {
					return;
				}

				int nextRisk = getRisk(nextPos);
				if (tool.usableRisks.contains(nextRisk)) {
					neighbours.add(new State(nextPos, tool));
				}
			}

			@Override
			public Iterable<State> getNeighbours() {
				List<State> neighbours = new ArrayList<>();
				addNeighbour(neighbours, 0, -1);
				addNeighbour(neighbours, 1, 0);
				addNeighbour(neighbours, 0, 1);
				addNeighbour(neighbours, -1, 0);

				int currentRisk = getRisk(position);
				for (Tool nextTool : Tool.values()) {
					if (nextTool != tool && nextTool.usableRisks.contains(currentRisk)) {
						neighbours.add(new State(position, nextTool));
					}
				}

				return neighbours;
			}

			@Override
			public int getCost() {
				return Math.abs(targetX - position.x) + Math.abs(targetY - position.y);
			}

			@Override
			public int getDistance(State neighbour) {
				return tool == neighbour.tool ? 1 : 7;
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
				return Objects.equals(position, state.position) &&
					tool == state.tool;
			}

			@Override
			public int hashCode() {
				return Objects.hash(position, tool);
			}
		}

		private final Map<Position, Integer> geologicalIndexes = new HashMap<>();
		private final int depth, targetX, targetY;

		public Grid(int depth, int targetX, int targetY) {
			this.depth = depth;
			this.targetX = targetX;
			this.targetY = targetY;
		}

		public int getGeologicalIndex(Position position) {
			Integer geologicalIndex = geologicalIndexes.get(position);
			if (geologicalIndex != null) {
				return geologicalIndex;
			}

			if ((position.x == 0 && position.y == 0) || (position.x == targetX && position.y == targetY)) {
				return 0;
			} else if (position.y == 0) {
				return position.x * 16807;
			} else if (position.x == 0) {
				return position.y * 48271;
			}

			geologicalIndex = getErosionLevel(position.add(-1, 0)) * getErosionLevel(position.add(0, -1));
			geologicalIndexes.put(position, geologicalIndex);
			return geologicalIndex;
		}

		public int getErosionLevel(Position position) {
			return (getGeologicalIndex(position) + depth) % 20183;
		}

		public int getRisk(Position position) {
			return getErosionLevel(position) % 3;
		}

		public int sumRisk() {
			int risk = 0;
			for (int x = 0; x <= targetX; x++) {
				for (int y = 0; y <= targetY; y++) {
					risk += getRisk(new Position(x, y));
				}
			}
			return risk;
		}

		public int getMinutes() {
			List<Grid.State> path = AStar.search(new State(new Position(0, 0), Tool.TORCH)).orElseThrow();

			int minutes = 0;
			for (int i = 0; i < path.size() - 1; i++) {
				minutes += path.get(i).getDistance(path.get(i + 1));
			}
			return minutes;
		}
	}
}

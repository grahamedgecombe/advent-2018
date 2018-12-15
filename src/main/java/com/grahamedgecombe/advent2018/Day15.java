package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public final class Day15 {
	public static void main(String[] args) throws IOException {
		Map map = Map.parse(AdventUtils.readLines("day15.txt"));
		System.out.println(map.getOutcome());
		System.out.println(map.getMinPowerOutcome());
	}

	private static final class Unit {
		private final boolean elf;
		private Position position;
		private int lastTick;
		private int hitPoints = 200;

		public Unit(boolean elf, Position position) {
			this.elf = elf;
			this.position = position;
		}
	}

	private static final class Position {
		private final int x, y;

		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Position add(int dx, int dy) {
			return new Position(x + dx, y + dy);
		}

		public List<Position> adjacent() {
			return Arrays.asList(
				add(0, -1),
				add(-1, 0),
				add(1, 0),
				add(0, 1)
			);
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

		@Override
		public String toString() {
			return "(" + x + ", " + y + ")";
		}
	}

	public static final class Map {
		public static Map parse(List<String> lines) {
			int width = lines.get(0).length();
			int height = lines.size();

			BitSet open = new BitSet(width * height);
			Unit[][] units = new Unit[height][width];

			for (int y = 0; y < height; y++) {
				String line = lines.get(y);
				if (line.length() != width) {
					throw new IllegalArgumentException();
				}

				for (int x = 0; x < width; x++) {
					char c = line.charAt(x);
					if (c == '.') {
						open.set(y * width + x);
					} else if (c == 'G' || c == 'E') {
						units[y][x] = new Unit(c == 'E', new Position(x, y));
						open.set(y * width + x);
					} else if (c != '#') {
						throw new IllegalArgumentException();
					}
				}
			}

			return new Map(width, height, open, units);
		}

		private final int width, height;
		private final BitSet open;
		private final Unit[][] originalUnits;
		private Unit[][] units;
		private int tick;

		private Map(int width, int height, BitSet open, Unit[][] units) {
			this.width = width;
			this.height = height;
			this.open = open;
			this.originalUnits = units;
			reset();
		}

		private void reset() {
			tick = 0;
			units = new Unit[height][width];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					Unit unit = originalUnits[y][x];
					if (unit != null) {
						units[y][x] = new Unit(unit.elf, unit.position);
					}
				}
			}
		}

		private List<Unit> getTargets(Unit self) {
			List<Unit> targets = new ArrayList<>();
			for (Unit[] row : units) {
				for (Unit unit : row) {
					if (unit != null && unit.elf != self.elf) {
						targets.add(unit);
					}
				}
			}
			return targets;
		}

		private boolean isOpen(Position position) {
			return open.get(position.y * width + position.x) && units[position.y][position.x] == null;
		}

		private Set<Position> getAdjacentOpenSquares(List<Unit> units) {
			Set<Position> squares = new HashSet<>();
			for (Unit unit : units) {
				squares.addAll(unit.position.adjacent());
			}
			return squares.stream()
				.filter(this::isOpen)
				.collect(Collectors.toSet());
		}

		private Optional<Unit> getAdjacentTarget(Unit self) {
			List<Unit> targets = new ArrayList<>();
			for (Position position : self.position.adjacent()) {
				targets.add(units[position.y][position.x]);
			}
			return targets.stream()
				.filter(u -> u != null && u.elf != self.elf)
				.min(Comparator.comparing(u -> u.hitPoints));
		}

		private class Node extends Bfs.Node<Node> {
			private final Position position;
			private final Set<Position> targets;

			public Node(Position position, Set<Position> targets) {
				this.position = position;
				this.targets = targets;
			}

			@Override
			public boolean isGoal() {
				return targets.contains(position);
			}

			@Override
			public Iterable<Node> getNeighbours() {
				return position.adjacent()
					.stream()
					.filter(p -> p.x >= 0 && p.y >= 0 && p.x < width && p.y < height && isOpen(p))
					.map(p -> new Node(p, targets))
					.collect(Collectors.toList());
			}

			@Override
			public boolean equals(Object o) {
				if (this == o) {
					return true;
				}
				if (o == null || getClass() != o.getClass()) {
					return false;
				}
				Node node = (Node) o;
				return Objects.equals(position, node.position);
			}

			@Override
			public int hashCode() {
				return Objects.hash(position);
			}
		}

		private Optional<Position> getNextPosition(Position start, Set<Position> targets) {
			return Bfs.searchAll(new Node(start, targets)).stream()
				.min(Comparator.comparingInt((ToIntFunction<List<Node>>) List::size)
					.thenComparing(path -> path.get(path.size() - 1).position.y)
					.thenComparing(path -> path.get(path.size() - 1).position.x))
				.map(p -> p.get(1).position);
		}

		private enum State {
			CONTINUE,
			ELF_DEATH,
			END
		}

		private State tick(boolean part1, int power) {
			tick++;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					Unit unit = units[y][x];
					if (unit == null || unit.lastTick == tick) {
						continue;
					}
					unit.lastTick = tick;

					Optional<Unit> target = getAdjacentTarget(unit);
					if (!target.isPresent()) {
						List<Unit> targets = getTargets(unit);
						if (targets.isEmpty()) {
							return State.END;
						}

						Set<Position> openSquares = getAdjacentOpenSquares(targets);
						if (openSquares.isEmpty()) {
							continue;
						}

						Optional<Position> nextPos = getNextPosition(unit.position, openSquares);
						if (!nextPos.isPresent()) {
							continue;
						}

						Position p = nextPos.get();
						if (units[p.y][p.x] != null) {
							throw new IllegalStateException();
						}
						units[y][x] = null;
						units[p.y][p.x] = unit;
						unit.position = p;

						target = getAdjacentTarget(unit);
					}

					if (target.isPresent()) {
						Unit t = target.get();
						t.hitPoints -= unit.elf ? power : 3;
						if (t.hitPoints <= 0) {
							if (!part1 && t.elf) {
								return State.ELF_DEATH;
							}
							units[t.position.y][t.position.x] = null;
						}
					}
				}
			}
			return State.CONTINUE;
		}

		private int sumHitPoints() {
			int sum = 0;
			for (Unit[] row : units) {
				for (Unit unit : row) {
					if (unit != null) {
						sum += unit.hitPoints;
					}
				}
			}
			return sum;
		}

		public int getOutcome() {
			reset();

			int rounds = 0;
			while (tick(true, 3) == State.CONTINUE) {
				rounds++;
			}
			return rounds * sumHitPoints();
		}

		public int getMinPowerOutcome() {
			for (int power = 4; power <= 200; power++) {
				reset();

				State state;
				int rounds = 0;
				while ((state = tick(false, power)) == State.CONTINUE) {
					rounds++;
				}
				if (state == State.END) {
					return rounds * sumHitPoints();
				}
			}

			throw new IllegalArgumentException();
		}

		@Override
		public String toString() {
			StringBuilder buf = new StringBuilder();
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					char c = open.get(y * width + x) ? '.' : '#';

					Unit unit = units[y][x];
					if (unit != null) {
						c = unit.elf ? 'E' : 'G';
					}

					buf.append(c);
				}

				buf.append(' ');

				boolean stripComma = false;
				for (Unit unit : units[y]) {
					if (unit == null) {
						continue;
					}

					buf.append(unit.elf ? 'E' : 'G');
					buf.append('(');
					buf.append(unit.hitPoints);
					buf.append("), ");
					stripComma = true;
				}
				if (stripComma) {
					buf.delete(buf.length() - 2, buf.length());
				} else {
					buf.delete(buf.length() - 1, buf.length());
				}
				buf.append("\n");
			}
			return buf.toString();
		}
	}
}

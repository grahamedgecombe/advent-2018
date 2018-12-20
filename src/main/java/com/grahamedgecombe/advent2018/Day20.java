package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

public final class Day20 {
	public static void main(String[] args) throws IOException {
		Graph graph = new Graph(AdventUtils.readString("day20.txt"));
		System.out.println(graph.getFurthestRoom());
		System.out.println(graph.getPaths());
	}

	public static final class Graph {
		private final class Position extends Bfs.Node<Position> {
			private final int x, y;

			public Position(int x, int y) {
				this.x = x;
				this.y = y;
			}

			public Position next(char c) {
				switch (c) {
					case 'N':
						return new Position(x, y - 1);
					case 'E':
						return new Position(x + 1, y);
					case 'S':
						return new Position(x, y + 1);
					case 'W':
						return new Position(x - 1, y);
				}

				throw new IllegalArgumentException();
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
				return "(" + x + ", " + y + ") " + isGoal();
			}

			@Override
			public boolean isGoal() {
				return edges.containsKey(this) || terminals.contains(this);
			}

			@Override
			public Iterable<Position> getNeighbours() {
				return edges.get(this);
			}
		}

		private final Position origin = new Position(0, 0);
		private final String regex;
		private final SetMultimap<Position, Position> edges = HashMultimap.create();
		private final Set<Position> terminals;
		private int index = 1;

		public Graph(String regex) {
			this.regex = regex;
			this.terminals = explore(Collections.singleton(origin));
		}

		public int getFurthestRoom() {
			return Bfs.searchAll(origin)
				.stream()
				.mapToInt(List::size)
				.max()
				.getAsInt() - 1;
		}

		public long getPaths() {
			return Bfs.searchAll(origin)
				.stream()
				.mapToInt(path -> path.size() - 1)
				.filter(doors -> doors >= 1000)
				.count();
		}

		private char prev() {
			return regex.charAt(index - 1);
		}

		private char consume() {
			return regex.charAt(index++);
		}

		private Set<Position> explore(Set<Position> states) {
			for (;;) {
				char c = consume();
				switch (c) {
					case '$':
					case '|':
					case ')':
						return states;
					case '(':
						Set<Position> next = new HashSet<>();
						do {
							next.addAll(explore(states));
						} while (prev() == '|');
						if (prev() != ')') {
							throw new IllegalArgumentException();
						}
						states = next;
						break;
					case 'N':
					case 'E':
					case 'S':
					case 'W':
						states = states.stream()
							.map(p -> {
								Position n = p.next(c);
								edges.put(p, n);
								return n;
							})
							.collect(Collectors.toSet());
						break;
					default:
						throw new IllegalArgumentException();
				}
			}
		}
	}
}


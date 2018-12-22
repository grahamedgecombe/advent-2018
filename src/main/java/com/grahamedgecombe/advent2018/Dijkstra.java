package com.grahamedgecombe.advent2018;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;

public final class Dijkstra {
	public static abstract class Node<T extends Node<T>> {
		public abstract boolean isGoal();
		public abstract Iterable<T> getNeighbours();
		public abstract int getDistance(T neighbour);
	}

	private static <T extends Node<T>> List<List<T>> search(T root, boolean all) {
		List<List<T>> paths = new ArrayList<>();

		Map<T, Integer> distance = new HashMap<>();
		PriorityQueue<T> queue = new PriorityQueue<>(Comparator.comparingInt(distance::get));
		Map<T, T> parents = new HashMap<>();

		distance.put(root, 0);
		queue.add(root);

		T current;
		while ((current = queue.poll()) != null) {
			if (current.isGoal()) {
				List<T> path = new ArrayList<>();

				T node = current;
				do {
					path.add(node);
				} while ((node = parents.get(node)) != null);

				Collections.reverse(path);
				paths.add(path);

				if (!all) {
					break;
				}
			}

			for (T neighbour : current.getNeighbours()) {
				int alt = distance.get(current) + current.getDistance(neighbour);
				if (alt < distance.getOrDefault(neighbour, Integer.MAX_VALUE)) {
					distance.put(neighbour, alt);
					queue.remove(neighbour);
					queue.add(neighbour);
					parents.put(neighbour, current);
				}
			}
		}

		return paths;
	}

	public static <T extends Node<T>> Optional<List<T>> search(T root) {
		List<List<T>> paths = search(root, false);

		if (paths.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(paths.get(0));
		}
	}

	public static <T extends Node<T>> List<List<T>> searchAll(T root) {
		return search(root, true);
	}

	private Dijkstra() {
		/* empty */
	}
}

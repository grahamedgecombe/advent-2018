package com.grahamedgecombe.advent2018;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

public final class Bfs {
	public static abstract class Node<T extends Node<T>> {
		public abstract boolean isGoal();
		public abstract Iterable<T> getNeighbours();
	}

	private static <T extends Node<T>> List<List<T>> search(T root, boolean all) {
		List<List<T>> paths = new ArrayList<>();

		Queue<T> queue = new ArrayDeque<>();
		Map<T, T> parents = new HashMap<>();

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
				if (parents.containsKey(neighbour)) {
					continue;
				}

				queue.add(neighbour);
				parents.put(neighbour, current);
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

	private Bfs() {
		/* empty */
	}
}

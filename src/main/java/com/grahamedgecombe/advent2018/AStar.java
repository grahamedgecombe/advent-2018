package com.grahamedgecombe.advent2018;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;

import com.google.common.primitives.Ints;

public final class AStar {
	public static abstract class Node<T extends Node<T>> {
		public abstract boolean isGoal();
		public abstract Iterable<T> getNeighbours();
		public abstract int getCost();
		public abstract int getDistance(T neighbour);
	}

	private static int saturatedAdd(int x, int y) {
		return Ints.saturatedCast((long) x + (long) y);
	}

	public static <T extends Node<T>> Optional<List<T>> search(T root) {
		Set<T> closed = new HashSet<>();
		Set<T> open = new HashSet<>();
		Map<T, T> parents = new HashMap<>();
		Map<T, Integer> fScore = new HashMap<>();
		Map<T, Integer> gScore = new HashMap<>();

		PriorityQueue<T> openQueue = new PriorityQueue<>(Comparator.comparingInt(node -> fScore.getOrDefault(node, Integer.MAX_VALUE)));

		open.add(root);
		openQueue.add(root);
		fScore.put(root, root.getCost());
		gScore.put(root, 0);

		T current;
		while ((current = openQueue.poll()) != null) {
			if (current.isGoal()) {
				List<T> path = new ArrayList<>();

				do {
					path.add(current);
				} while ((current = parents.get(current)) != null);

				Collections.reverse(path);
				return Optional.of(path);
			}

			open.remove(current);
			closed.add(current);

			for (T neighbour : current.getNeighbours()) {
				if (closed.contains(neighbour)) {
					continue;
				}

				int tentativeGScore = saturatedAdd(gScore.getOrDefault(current, Integer.MAX_VALUE), current.getDistance(neighbour));
				if (!open.contains(neighbour)) {
					open.add(neighbour);
				} else if (tentativeGScore >= gScore.getOrDefault(neighbour, Integer.MAX_VALUE)) {
					continue;
				} else {
					openQueue.remove(neighbour);
				}

				parents.put(neighbour, current);
				fScore.put(neighbour, tentativeGScore + neighbour.getCost());
				gScore.put(neighbour, tentativeGScore);

				openQueue.add(neighbour);
			}
		}

		return Optional.empty();
	}
}

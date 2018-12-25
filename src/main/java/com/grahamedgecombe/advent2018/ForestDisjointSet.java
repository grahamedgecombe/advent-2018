package com.grahamedgecombe.advent2018;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * A {@link DisjointSet} implementation using the disjoint-set forest data
 * structure as described in CLRS chapter 21. This class is not thread-safe.
 * @author Graham Edgecombe
 */
public final class ForestDisjointSet<T> implements DisjointSet<T> {
	private static final class Node<T> implements Partition<T> {
		private final List<Node<T>> children = new ArrayList<>();
		private final T value;
		private Node<T> parent = this;
		private int rank = 0;

		private Node(T value) {
			this.value = value;
		}

		private void setParent(Node<T> parent) {
			this.parent = parent;
			this.parent.children.add(this);
		}

		private Node<T> find() {
			if (parent != this) {
				parent = parent.find();
			}
			return parent;
		}

		@Override
		public Iterator<T> iterator() {
			return new NodeIterator<>(find());
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object other) {
			if (other == null || getClass() != other.getClass()) {
				return false;
			}

			Node<T> node = (Node<T>) other;
			return find() == node.find();
		}

		@Override
		public int hashCode() {
			return find().value.hashCode();
		}
	}

	private static class NodeIterator<T> implements Iterator<T> {
		private final Queue<Node<T>> queue = new ArrayDeque<>();

		public NodeIterator(Node<T> root) {
			this.queue.add(root);
		}

		@Override
		public boolean hasNext() {
			return !queue.isEmpty();
		}

		@Override
		public T next() {
			Node<T> node = queue.poll();
			if (node == null) {
				throw new NoSuchElementException();
			}

			queue.addAll(node.children);
			return node.value;
		}
	}

	private static class SetIterator<T> implements Iterator<Partition<T>> {
		private final Iterator<Node<T>> it;

		public SetIterator(ForestDisjointSet<T> set) {
			this.it = new HashSet<>(set.nodes.values()).iterator();
		}

		@Override
		public boolean hasNext() {
			return it.hasNext();
		}

		@Override
		public Partition<T> next() {
			return it.next();
		}
	}

	private final Map<T, Node<T>> nodes = new HashMap<>();
	private int elements = 0, partitions = 0;

	@Override
	public Partition<T> add(T x) {
		Node<T> node = nodes.get(x);
		if (node != null) {
			return node.find();
		}

		elements++;
		partitions++;

		nodes.put(x, node = new Node<>(x));
		return node;
	}

	@Override
	public Partition<T> get(T x) {
		return get0(x);
	}

	private Node<T> get0(T x) {
		Node<T> node = nodes.get(x);
		if (node == null) {
			return null;
		}

		return node.find();
	}

	@Override
	public void union(Partition<T> x, Partition<T> y) {
		Node<T> xRoot = ((Node<T>) x).find();
		Node<T> yRoot = ((Node<T>) y).find();

		if (xRoot == yRoot) {
			return;
		}

		if (xRoot.rank < yRoot.rank) {
			xRoot.setParent(yRoot);
		} else if (xRoot.rank > yRoot.rank) {
			yRoot.setParent(xRoot);
		} else {
			yRoot.setParent(xRoot);
			xRoot.rank++;
		}

		partitions--;
	}

	@Override
	public int elements() {
		return elements;
	}

	@Override
	public int partitions() {
		return partitions;
	}

	@Override
	public Iterator<Partition<T>> iterator() {
		return new SetIterator<>(this);
	}
}

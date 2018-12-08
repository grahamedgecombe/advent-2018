package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public final class Day8 {
	public static void main(String[] args) throws IOException {
		Node root = parseTree(AdventUtils.readString("day8.txt"));
		System.out.println(root.sumMetadata());
		System.out.println(root.value());
	}

	public static final class Node {
		private final List<Node> children = new ArrayList<>();
		private final List<Integer> metadata = new ArrayList<>();

		public int sumMetadata() {
			return metadata.stream().mapToInt(i -> i).sum() +
				children.stream().mapToInt(Node::sumMetadata).sum();
		}

		public int value() {
			if (children.isEmpty()) {
				return metadata.stream().mapToInt(i -> i).sum();
			}
			return metadata.stream()
				.mapToInt(i -> i - 1)
				.filter(i -> i >= 0 && i < children.size())
				.map(i -> children.get(i).value())
				.sum();
		}
	}

	public static Node parseTree(String str) {
		Queue<Integer> in = Arrays.stream(str.split(" "))
			.map(Integer::parseInt)
			.collect(Collectors.toCollection(ArrayDeque::new));
		return parseNode(in);
	}

	private static Node parseNode(Queue<Integer> in) {
		Node node = new Node();

		int childEntries = in.poll();
		int metadataEntries = in.poll();

		for (int i = 0; i < childEntries; i++) {
			node.children.add(parseNode(in));
		}

		for (int i = 0; i < metadataEntries; i++) {
			node.metadata.add(in.poll());
		}

		return node;
	}
}

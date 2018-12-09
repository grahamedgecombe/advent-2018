package com.grahamedgecombe.advent2018;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Day9 {
	private static final Pattern PATTERN = Pattern.compile("^([0-9]+) players; last marble is worth ([0-9]+) points$");

	public static void main(String[] args) throws IOException {
		Matcher matcher = PATTERN.matcher(AdventUtils.readString("day9.txt"));
		if (!matcher.matches()) {
			throw new IllegalArgumentException();
		}
		int players = Integer.parseInt(matcher.group(1));
		int lastMarble = Integer.parseInt(matcher.group(2));
		System.out.println(getHighScore(players, lastMarble));
		System.out.println(getHighScore(players, lastMarble * 100));
	}

	private static final class Node {
		private Node next;
		private Node previous;
		private int value;

		public Node(int value) {
			this.value = value;
			this.next = this;
			this.previous = this;
		}

		private Node() {
			/* empty */
		}

		public Node add(int value) {
			Node n = new Node();
			n.value = value;
			n.previous = this;
			n.next = this.next;
			this.next.previous = n;
			this.next = n;
			return n;
		}

		public Node previous(int n) {
			if (n == 0) {
				return this;
			}
			return previous.previous(n - 1);
		}

		public Node remove() {
			this.previous.next = this.next;
			this.next.previous = this.previous;
			return this.next;
		}
	}

	public static long getHighScore(int players, int lastMarble) {
		int nextMarble = 0;
		Node current = new Node(nextMarble++);

		long[] score = new long[players];

		for (int i = 0; nextMarble <= lastMarble; i = (i + 1) % players, nextMarble++) {
			if (nextMarble % 23 == 0) {
				Node n = current.previous(7);
				score[i] += nextMarble + n.value;
				current = n.remove();
			} else {
				current = current.next.add(nextMarble);
			}
		}

		return Arrays.stream(score).max().getAsLong();
	}
}

package com.grahamedgecombe.advent2018;

/**
 * Represents a disjoint set data structure, which keeps track of a set of
 * elements partitioned into a number of non-overlapping subsets.
 * @author Graham Edgecombe
 */
public interface DisjointSet<T> extends Iterable<DisjointSet.Partition<T>> {
	// TODO fill in Javadoc
	interface Partition<T> extends Iterable<T> {
		/* empty */
	}

	Partition<T> add(T x);
	Partition<T> get(T x);
	void union(Partition<T> x, Partition<T> y);
	int elements();
	int partitions();
}

package graph;

import java.util.Collection;

/**
 * determines the functionality of a traversable graph
 * A graph is a collection of Nodes
 * Each Node contains concrete data & a reference to parent node
 */
public interface Traversable<T> {
	GraphNode<T> getOrigin();

	Collection<GraphNode<T>> getReachableNodes(final GraphNode<T> source);
}
package graph;

import java.util.Collection;

public interface Traversable<T> {
	GraphNode<T> getOrigin();

	Collection<GraphNode<T>> getReachableNodes(final GraphNode<T> source, boolean diagonal);

	Collection<GraphNode<T>> getReachableNodes(final GraphNode<T> source);

}
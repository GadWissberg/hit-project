package handlers.types.first;

import graph.GraphNode;
import graph.Traversable;
import graph.TraverseLogic;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class FindAdjacentAlgorithm<T> extends TraverseLogic<T> {

	@NotNull
	public AbstractList<T> findConnectedComponents(@NotNull final Traversable<T> graph,
												   @NotNull final T source,
												   @NotNull final Set<T> seenIndices) {
		return findConnectedComponents(graph, source, seenIndices, true);
	}

	@NotNull
	public AbstractList<T> findConnectedComponents(@NotNull final Traversable<T> graph,
												   @NotNull final T source,
												   @NotNull final Set<T> seenIndices,
												   final boolean diagonal) {
		// get ThreadLocal collections
		Collection<GraphNode<T>> grey = this.greyCollection.get();
		Set<GraphNode<T>> black = this.blackCollection.get();

		pushToLocalStack(new GraphNode<>(source));
		black.clear();

		// DFS
		while (!grey.isEmpty()) {
			GraphNode<T> removedNode = popFromLocalGrayStack();
			black.add(removedNode);
			Optional.ofNullable(removedNode).ifPresent(node -> seenIndices.add(node.getData()));
			Collection<GraphNode<T>> reachableNodes = graph.getReachableNodes(removedNode, diagonal);
			boolean areNextReachablesMine = reachableNodes
					.stream()
					.noneMatch(node -> seenIndices.contains(node.getData()) && !black.contains(node) && !grey.contains(node));

			// Making sure this thread is not on the same component of another thread.
			if (areNextReachablesMine) {
				reachableNodes.stream()
						.filter(graphNode -> !black.contains(graphNode) && !grey.contains(graphNode))
						.forEach(this::pushToLocalStack);
			} else {
				black.forEach(node -> seenIndices.remove(node.getData()));
				black.clear();
				break;
			}

		}
		// extract Index data from each graph node in black collection
		return black.stream().map(GraphNode::getData).collect(Collectors.toCollection(ArrayList::new));
	}

}

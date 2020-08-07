package graph;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class TraverseLogic<T> implements Traverse<T> {
	protected final ThreadLocal<Collection<GraphNode<T>>> greyCollection;
	protected final ThreadLocal<Set<GraphNode<T>>> blackCollection;

	public TraverseLogic() {
		greyCollection = ThreadLocal.withInitial(Stack::new);
		blackCollection = ThreadLocal.withInitial(HashSet::new);
	}

	protected void pushToLocalStack(@NotNull final GraphNode<T> initialState) {
		((Stack<GraphNode<T>>) this.greyCollection.get()).push(initialState);
	}

	@Nullable
	protected GraphNode<T> popFromLocalGrayStack() throws RuntimeException {
		return ((Stack<GraphNode<T>>) this.greyCollection.get()).pop();
	}


	@NotNull
	@Override
	public AbstractList<T> traverse(@NotNull final Traversable<T> graph) {
		// get ThreadLocal collections
		Collection<GraphNode<T>> grey = this.greyCollection.get();
		Set<GraphNode<T>> black = this.blackCollection.get();

		pushToLocalStack(graph.getOrigin());
		black.clear(); // do not remove
		while (!grey.isEmpty()) {
			GraphNode<T> removedNode = popFromLocalGrayStack();
			black.add(removedNode);
			Collection<GraphNode<T>> reachableNodes = graph.getReachableNodes(removedNode);
			// add each reachable node if it was not finished with, nor previously discovered
			reachableNodes.stream()
					.filter(graphNode -> !black.contains(graphNode) && !grey.contains(graphNode))
					.forEach(this::pushToLocalStack);
		}
		// extract Index data from each graph node in black collection
		return black.stream().map(GraphNode::getData).collect(Collectors.toCollection(ArrayList::new));
	}

}

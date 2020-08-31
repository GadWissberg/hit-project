package graph;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public abstract class TraverseLogic<T> implements Traverse<T> {
	protected final ThreadLocal<Collection<GraphNode<T>>> greyCollection;
	protected final ThreadLocal<Set<GraphNode<T>>> blackCollection;

	public TraverseLogic() {
		greyCollection = ThreadLocal.withInitial(Stack::new);
		blackCollection = ThreadLocal.withInitial(HashSet::new);
	}

	protected void pushToLocalStack(final GraphNode<T> initialState) {
		((Stack<GraphNode<T>>) this.greyCollection.get()).push(initialState);
	}

	@Nullable
	protected GraphNode<T> popFromLocalGrayStack() throws RuntimeException {
		return ((Stack<GraphNode<T>>) this.greyCollection.get()).pop();
	}


}

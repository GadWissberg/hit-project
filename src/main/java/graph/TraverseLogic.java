package graph;

import handlers.routes.RoutesResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

public class TraverseLogic<T> implements Traverse<T> {
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

	public RoutesResult<T> findRoutes(final Traversable<T> graph,
									  final T source,
									  final T dest,
									  final ThreadPoolExecutor executor,
									  final Set<GraphNode<T>> blackNodes) {
		RoutesResult<T> routes = new RoutesResult<>();
		LinkedHashSet<GraphNode<T>> currentRoute = new LinkedHashSet<>();
		blackCollection.set(new HashSet<>(blackNodes));
		Set<GraphNode<T>> myBlackNodes = blackCollection.get();
		Stack<GraphNode<T>> grayNodes = (Stack<GraphNode<T>>) greyCollection.get();
		grayNodes.push(new GraphNode<>(source));

		// Run through linear routes 'till you reach a fork or end.
		while (grayNodes.size() == 1) {
			GraphNode<T> removedNode = grayNodes.pop();

			GraphNode<T> destNode = new GraphNode<>(dest);

			// If we reached destination, current thread can finish.
			if (removedNode.equals(destNode)) {
				currentRoute.add(destNode);
				routes.getResult().add(currentRoute);
				return routes;
			}


			myBlackNodes.add(removedNode);
			currentRoute.add(removedNode);
			Collection<GraphNode<T>> reachableNodes = graph.getReachableNodes(removedNode);


			reachableNodes.stream()
					.filter(graphNode -> !myBlackNodes.contains(graphNode) && !grayNodes.contains(graphNode))
					.forEach(grayNodes::push);
		}

		// In case we reached end of route.
		if (grayNodes.size() == 0) {
			return routes;
		}


		// Fork.
		ArrayList<CompletableFuture<RoutesResult<T>>> futures = new ArrayList<>();
		grayNodes.forEach(node -> {
			CompletableFuture<RoutesResult<T>> future = CompletableFuture.supplyAsync(() ->
					(findRoutes(graph, node.getData(), dest, executor, myBlackNodes)), executor);
			futures.add(future);
		});

		// Handle fork result.
		futures.forEach(future -> {
			try {
				RoutesResult<T> subRoutes = future.get();
				subRoutes.getResult().forEach(subRoute -> {
					LinkedHashSet<GraphNode<T>> route = new LinkedHashSet<>(currentRoute);
					route.addAll(subRoute);
					routes.getResult().add(route);
				});
				System.out.println(Thread.currentThread().getId() + " | " + subRoutes.toString());
			} catch (final InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		});

		// In case we didn't have a fork.
		if (routes.getResult().size() == 0) {
			routes.getResult().add(currentRoute);
		}
		return routes;
	}
}

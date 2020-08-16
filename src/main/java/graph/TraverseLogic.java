package graph;

import handlers.RoutesResult;
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
	public AbstractList<T> findConnectedComponents(@NotNull final Traversable<T> graph) {
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

	public RoutesResult<T> findShortestPaths(final Traversable<T> graph, final T source, final T destination) {
		RoutesResult<T> routes = new RoutesResult<>();
		Stack<GraphNode<T>> myBlackNodes = new Stack<>();
		Stack<GraphNode<T>> grayNodes = new Stack<>();
		GraphNode<T> sourceNode = new GraphNode<>(source);
		grayNodes.push(sourceNode);

		Map<GraphNode<T>, GraphNode<T>> predecessors = new HashMap<>();
		Map<GraphNode<T>, Integer> distances = new HashMap<>();
		distances.put(sourceNode, 0);
		int distance = 0;
		int minDistance = Integer.MAX_VALUE;

		GraphNode<T> destNode = new GraphNode<>(destination);
		while (grayNodes.size() > 0) {
			GraphNode<T> removedNode = grayNodes.pop();
			myBlackNodes.push(removedNode);
			Collection<GraphNode<T>> reachableNodes = graph.getReachableNodes(removedNode);
			boolean addReachablesToGray = true;

			int currentDistance = distances.get(removedNode) + 1;

			// Whether destination is reachable.
			if (reachableNodes.stream().anyMatch(node -> node.equals(destNode))) {
				addReachablesToGray = false;

				// Whether current route is accepted.
				if (currentDistance <= minDistance) {

					// Clear previous routes, because a shorter has been found.
					if (currentDistance < minDistance) {
						routes.getResult().clear();
					}
					ArrayList<GraphNode<T>> shortestPath = new ArrayList<>();
					minDistance = currentDistance;

					// Trace back that short route.
					GraphNode<T> predecessor = removedNode;
					shortestPath.add(0, destNode);
					shortestPath.add(0, removedNode);
					do {
						predecessor = predecessors.get(predecessor);
						if (predecessor != null) {
							shortestPath.add(0, predecessor);
						}
					}
					while (predecessor != null && !predecessor.equals(sourceNode));
					routes.getResult().add(shortestPath);
				}

			}

			// No need to continue this route because it's long.
			if (currentDistance > minDistance) {
				addReachablesToGray = false;
			}

			// Whether to continue this route.
			if (addReachablesToGray) {
				reachableNodes.stream()
						.filter(graphNode -> !myBlackNodes.contains(graphNode)
								&& !grayNodes.contains(graphNode)
								&& !graphNode.equals(destNode))
						.forEach(node -> {
							grayNodes.push(node);
							predecessors.put(node, removedNode);
							distances.put(node, distances.get(removedNode) + 1);
						});
			} else {

				// If we didn't continue this route, we can go back 'till the current sub-tree has been forked.
				GraphNode<T> black;
				do {
					black = myBlackNodes.pop();
				}
				while (!grayNodes.isEmpty()
						&& !myBlackNodes.isEmpty()
						&& !predecessors.get(grayNodes.peek()).equals(predecessors.get(black)));
			}

		}

		return routes;

	}


}

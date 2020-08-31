package handlers.types.third;

import graph.GraphNode;
import graph.Traversable;
import graph.TraverseLogic;
import handlers.RoutesResult;

import java.util.*;

public class ShortestPathsAlgorithm<T> extends TraverseLogic<T> {
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

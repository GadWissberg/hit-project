package handlers;

import graph.GraphNode;
import lombok.Getter;
import test.Index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RoutesResult<T> {
	List<Collection<GraphNode<T>>> result = new ArrayList<>();

	@Override
	public String toString() {
		return "RoutesResult{" +
				"result=" + result +
				'}';
	}

	public void sort() {
		result.forEach(route ->
		{
			LinkedHashSet<GraphNode<T>> sorted = route.stream().sorted((tGraphNode, t1) -> Utils.sortNodes((GraphNode<Index>) tGraphNode, (GraphNode<Index>) t1)).collect(Collectors.toCollection(LinkedHashSet::new));
		});
	}

}

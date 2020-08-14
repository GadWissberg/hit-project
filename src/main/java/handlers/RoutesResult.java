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
			LinkedHashSet<GraphNode<T>> sorted = route.stream().sorted((GraphNode<T> s1, GraphNode<T> s2) -> {
				int res = 0;
				Index data1 = (Index) s1.getData();
				Index data2 = (Index) s2.getData();
				if (data1 != null && data2 != null) {
					if (data1.row > data2.row) {
						res = 1;
					} else if (data1.row < data2.row) {
						res = -1;
					} else if (data1.column < data2.column) {
						res = 1;
					} else {
						res = -1;
					}
				}
				return res;
			}).collect(Collectors.toCollection(LinkedHashSet::new));
		});
	}
}

package handlers;

import graph.GraphNode;
import test.Index;

public final class Utils {
	public static int sortNodes(final GraphNode<Index> s1, final GraphNode<Index> s2) {
		int res = 0;
		Index data1 = s1.getData();
		Index data2 = s2.getData();
		if (data1 != null && data2 != null) {
			if (data1.row > data2.row) {
				res = -1;
			} else if (data1.row < data2.row) {
				res = 1;
			} else if (data1.column < data2.column) {
				res = -1;
			} else {
				res = 1;
			}
		}
		return res;
	}
}

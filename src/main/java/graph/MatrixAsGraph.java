package graph;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import test.Index;
import test.Matrix;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 1. Primitive 2D array-> Matrix Type + Index
 * 2. Interface that represents a traversable graph ->  matrix to traversable graph
 * 3. The traversing Logic!
 * return to the problem definition: given a 2D binary matrix, return all
 * subsets of 1's (discover all SCC groups)
 */


// This class represents a matrix that can be traversed as a graph. i.e.,
// has an origin and the ability to get reachable nodes from a given source
public class MatrixAsGraph implements Traversable<Index> {
	private final Matrix matrix;

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private Index root;

	public MatrixAsGraph(@NotNull final Matrix matrix, final Index source) {
		this.matrix = matrix;
		this.root = source;
	}

	@Override
	public GraphNode<Index> getOrigin() {
		if (this.root == null) throw new NullPointerException("Source index NOT initialized");
		return new GraphNode<>(this.root);
	}

	@Override
	public Collection<GraphNode<Index>> getReachableNodes(final GraphNode<Index> source) {
		return this.matrix.getReachables(source.getData()).stream().filter(index -> matrix.getValue(index) == 1)
				.map(neighbor -> new GraphNode<>(neighbor, source)).collect(Collectors.toList());
	}


}

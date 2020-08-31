package graph;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import test.Index;
import test.Matrix;

import java.util.Collection;
import java.util.stream.Collectors;


public class MatrixAsGraph implements Traversable<Index> {
	public static final String MSG_NOT_INITIALIZED = "Source index NOT initialized";
	private final Matrix matrix;

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private Index root;

	public MatrixAsGraph(@NotNull final Matrix matrix, final Index source) {
		this.matrix = matrix;
		this.root = source;
	}

	public MatrixAsGraph(@NotNull final Matrix matrix) {
		this(matrix, null);
	}

	@Override
	public GraphNode<Index> getOrigin() {
		if (this.root == null) throw new NullPointerException(MSG_NOT_INITIALIZED);
		return new GraphNode<>(this.root);
	}

	@Override
	public Collection<GraphNode<Index>> getReachableNodes(final GraphNode<Index> source, final boolean diagonal) {
		return this.matrix
				.getReachables(source.getData(), diagonal)
				.stream()
				.map(neighbor -> new GraphNode<>(neighbor, source))
				.collect(Collectors.toList());
	}

	@Override
	public Collection<GraphNode<Index>> getReachableNodes(final GraphNode<Index> source) {
		return getReachableNodes(source, true);
	}


}

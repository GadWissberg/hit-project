package graph;

import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public class GraphNode<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Getter(AccessLevel.PUBLIC)
	private @Nullable T data;

	private @Nullable GraphNode<T> parent;

	public GraphNode() {
		this(null);
	}

	public GraphNode(@Nullable final T data) {
		this(data, null);
	}

	public GraphNode(@Nullable final T data, @Nullable final GraphNode<T> parent) {
		this.data = data;
		this.parent = parent;
	}


	@NotNull
	public GraphNode<T> setData(final T data) {
		this.data = data;
		return this;
	}

	@Nullable
	public GraphNode<T> getParent() {
		return parent;
	}

	@NotNull
	public GraphNode<T> setParent(final GraphNode<T> parent) {
		this.parent = parent;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof GraphNode)) return false;
		GraphNode<?> state1 = (GraphNode<?>) o;
		return Objects.equals(data, state1.data);
	}

	@Override
	public int hashCode() {
		return data != null ? data.hashCode() : 0;
	}

	@Override
	public String toString() {
		return Optional.ofNullable(data).isPresent() ? data.toString() : super.toString();
	}

}

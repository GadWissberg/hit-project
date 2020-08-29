package graph;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

/**
 * example for interface's generics: some class that extends MatrixAsGraph
 * MatrixAsGraph implements Traversable<Index>
 * We should return all the groups of 1's- List<Index>
 */
public interface GenericTraverse<R, V extends Traversable<R>> {
	Collection<R> findConnectedComponents(@NotNull final V Graph, R index, Set<R> seenIndices);

	Collection<R> findConnectedComponents(@NotNull final V Graph, R index, Set<R> seenIndices, final boolean diagonal);
}

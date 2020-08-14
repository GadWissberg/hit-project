package graph;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * example for interface's generics: some class that extends MatrixAsGraph
 * MatrixAsGraph implements Traversable<Index>
 * We should return all the groups of 1's- List<Index>
 */
public interface GenericTraverse<R, V extends Traversable<R>> {
	Collection<R> findConnectedComponents(@NotNull final V Graph);
}

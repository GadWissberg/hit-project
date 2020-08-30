package handlers.types.first;

import graph.MatrixAsGraph;
import graph.TraverseLogic;
import handlers.BaseHandler;
import org.jetbrains.annotations.NotNull;
import test.Index;
import test.Matrix;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class AdjacentHandler extends BaseHandler<Index> {


	private static final String RESPONSE_RESULT = "result";
	private static final int NUMBER_OF_THREADS = 4;

	public AdjacentHandler(final Matrix matrix) {
		this.setMatrix(matrix);
	}

	public AdjacentHandler() {
	}

	@Override
	public void begin(final ThreadPoolExecutor executor, final ObjectOutputStream objectOutputStream) {
		Matrix matrix = getMatrix();
		if (matrix != null) {
			List<HashSet<Index>> indexList = findAllComponents(matrix, executor);
			try {
				objectOutputStream.writeObject(indexList);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	@NotNull
	public List<HashSet<Index>> findAllComponents(final Matrix matrix, final ThreadPoolExecutor executor) {
		return findAllComponents(matrix, executor, true);
	}

	@NotNull
	public List<HashSet<Index>> findAllComponents(final Matrix matrix,
												  final ThreadPoolExecutor executor,
												  final boolean diagonal) {
		TraverseLogic<Index> logic = new TraverseLogic<>();
		MatrixAsGraph graph = new MatrixAsGraph(matrix);
		List<HashSet<Index>> indexList = Collections.synchronizedList(new ArrayList<>());
		Set<Index> seenIndices = Collections.newSetFromMap(new ConcurrentHashMap<>());
		int[][] primitiveMatrix = matrix.getPrimitiveMatrix();
		Future<?> topLeft = executor.submit(() -> {
			findComponents(
					graph,
					seenIndices,
					indexList,
					diagonal,
					0, primitiveMatrix.length / 2,
					0, primitiveMatrix[0].length / 2
			);
		});
		Future<?> topRight = executor.submit(() -> findComponents(
				graph,
				seenIndices,
				indexList,
				diagonal,
				0, primitiveMatrix.length / 2,
				primitiveMatrix[0].length / 2, primitiveMatrix[0].length
		));
		Future<?> bottomLeft = executor.submit(() -> findComponents(
				graph,
				seenIndices,
				indexList,
				diagonal,
				primitiveMatrix.length / 2, primitiveMatrix.length,
				0, primitiveMatrix[0].length / 2
		));
		Future<?> bottomRight = executor.submit(() -> findComponents(
				graph,
				seenIndices,
				indexList,
				diagonal,
				primitiveMatrix.length / 2, primitiveMatrix.length,
				primitiveMatrix[0].length / 2, primitiveMatrix[0].length
		));
		try {
			topLeft.get();
			topRight.get();
			bottomLeft.get();
			bottomRight.get();
		} catch (final InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return indexList;
	}

	private void findComponents(final MatrixAsGraph graph,
								final Set<Index> seenIndices,
								final List<HashSet<Index>> indexList,
								final boolean diagonal,
								final int rowStart,
								final int rowEnd,
								final int columnStart,
								final int columnEnd) {
		FindAdjacentAlgorithm<Index> algorithm = new FindAdjacentAlgorithm<>();
		for (int i = rowStart; i < rowEnd; i++) {
			for (int j = columnStart; j < columnEnd; j++) {
				final Index source = new Index(i, j);
				if (getMatrix().getValue(source) == 1) {
					synchronized (AdjacentHandler.this) {
						if (seenIndices.contains(source)) {
							continue;
						} else {
							seenIndices.add(source);
						}
					}
					HashSet<Index> hashSet = new HashSet<>(algorithm.findConnectedComponents(graph, source, seenIndices, diagonal));
					if (!hashSet.isEmpty()) {
						indexList.add(hashSet);
						seenIndices.addAll(hashSet);
					}
				}
			}
		}
	}

}

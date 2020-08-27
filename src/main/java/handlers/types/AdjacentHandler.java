package handlers.types;

import graph.MatrixAsGraph;
import graph.Traverse;
import graph.TraverseLogic;
import handlers.BaseHandler;
import org.jetbrains.annotations.NotNull;
import test.Index;
import test.Matrix;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

public class AdjacentHandler extends BaseHandler<Index> {


	private static final String RESPONSE_RESULT = "result";


	@Override
	public void begin(final ThreadPoolExecutor executor, final ObjectOutputStream objectOutputStream) {
		Matrix matrix = getMatrix();
		if (matrix != null) {
			List<HashSet<Index>> indexList = findAllComponents(matrix);

			try {
				objectOutputStream.writeObject(indexList);
			} catch (final IOException e) {
				e.printStackTrace();
			}

		}
	}

	@NotNull
	public List<HashSet<Index>> findAllComponents(final Matrix matrix) {
		return findAllComponents(matrix, true);
	}

	@NotNull
	public List<HashSet<Index>> findAllComponents(final Matrix matrix, final boolean diagonal) {
		TraverseLogic<Index> logic = new TraverseLogic<>();
		MatrixAsGraph graph = new MatrixAsGraph(matrix);

		List<HashSet<Index>> indexList = Collections.synchronizedList(new ArrayList<>());
		HashSet<Index> seenIndexes = new HashSet<>();

		Traverse<Index> algorithm = new TraverseLogic<>();
		int[][] primitiveMatrix = matrix.getPrimitiveMatrix();
		for (int i = 0; i < primitiveMatrix.length; i++) {
			for (int j = 0; j < primitiveMatrix[0].length; j++) {
				final Index index = new Index(i, j);
				if (matrix.getValue(index) == 1 && !seenIndexes.contains(index)) {
					graph.setRoot(index);
					Collection<Index> list = algorithm.findConnectedComponents(graph, diagonal);
					HashSet<Index> hashSet = new HashSet<>(list);
					indexList.add(hashSet);
					seenIndexes.addAll(hashSet);
				}
			}
		}
		return indexList;
	}

}

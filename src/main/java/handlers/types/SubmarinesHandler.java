package handlers.types;

import graph.MatrixAsGraph;
import graph.TraverseLogic;
import handlers.BaseHandler;
import handlers.RoutesResult;
import test.Index;
import test.Matrix;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public class SubmarinesHandler extends BaseHandler<Index> {

	@Override
	public void begin(final ThreadPoolExecutor executor, final ObjectOutputStream objectOutputStream) {
		TraverseLogic<Index> logic = new TraverseLogic<>();
		MatrixAsGraph graph = new MatrixAsGraph(getMatrix());

		Matrix matrix = getMatrix();
		if (matrix != null) {

			List<HashSet<Index>> indexList = Collections.synchronizedList(new ArrayList<>());
			HashSet<Index> seenIndexes = new HashSet<>();

			TraverseLogic<Index> algorithm = new TraverseLogic<>();
			int[][] primitiveMatrix = matrix.getPrimitiveMatrix();
			for (int row = 0; row < primitiveMatrix.length; row++) {
				for (int column = 0; column < primitiveMatrix[0].length; column++) {
					final Index index = new Index(row, column);
					if (matrix.getValue(index) == 1) {
						graph.setRoot(index);
						RoutesResult<Index> list = algorithm.checkSubmarines(graph, index);
					}
				}
			}


		}


	}

}

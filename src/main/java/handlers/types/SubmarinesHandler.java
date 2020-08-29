package handlers.types;

import graph.MatrixAsGraph;
import graph.TraverseLogic;
import handlers.BaseHandler;
import org.jetbrains.annotations.NotNull;
import test.Index;
import test.Matrix;

import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

public class SubmarinesHandler extends BaseHandler<Index> {

	public static final int FAIL = -1;

	@Override
	public void begin(final ThreadPoolExecutor executor, final ObjectOutputStream objectOutputStream) {
		TraverseLogic<Index> logic = new TraverseLogic<>();
		MatrixAsGraph graph = new MatrixAsGraph(getMatrix());

		Matrix matrix = getMatrix();
		@NotNull List<HashSet<Index>> components;
		final int[] result = {0};
		if (matrix != null) {
			AdjacentHandler adjacentHandler = new AdjacentHandler();
			components = adjacentHandler.findAllComponents(matrix, executor, false);
			components.forEach(component -> {
				if (result[0] > -1) {
					if (component.size() > 1) {
						Object[] hashSet = component.toArray();
						Optional<Index> topMostIndex;
						topMostIndex = component.stream().min(Comparator.comparingInt(index -> index.row));
						if (topMostIndex.isPresent()) {
							Index finalTopMostIndex = topMostIndex.get();
							Optional<Index> topLeftCorner = component.stream()
									.filter(ind -> finalTopMostIndex.row >= ind.row)
									.min(Comparator.comparingInt(index -> index.column));
							if (topLeftCorner.isPresent()) {
								Optional<Index> bottomMostIndex = component.stream().max(Comparator.comparingInt(index -> index.row));
								Optional<Index> bottomRightCorner = component.stream()
										.filter(ind -> bottomMostIndex.get().row <= ind.row)
										.max(Comparator.comparingInt(index -> index.column));
								if (bottomRightCorner.isPresent()) {
									int rectSize = (bottomRightCorner.get().row - topLeftCorner.get().row + 1) * (bottomRightCorner.get().column - topLeftCorner.get().column + 1);
									if (rectSize == component.size()) {
										result[0]++;
									} else {
										result[0] = FAIL;
									}
								}
							}
						}
					} else {
						result[0] = FAIL;
					}
				}
			});
			System.out.println(Arrays.toString(result));
		}


	}

}

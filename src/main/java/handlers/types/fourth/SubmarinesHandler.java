package handlers.types.fourth;

import graph.MatrixAsGraph;
import handlers.BaseHandler;
import handlers.types.first.AdjacentHandler;
import org.jetbrains.annotations.NotNull;
import test.Index;
import test.Matrix;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public class SubmarinesHandler extends BaseHandler<Index> {

	public static final int FAIL = -1;

	@Override
	public void begin(final ThreadPoolExecutor executor, final ObjectOutputStream objectOutputStream) {
		SubmarinesAlgorithm<Index> logic = new SubmarinesAlgorithm<>();
		MatrixAsGraph graph = new MatrixAsGraph(getMatrix());

		Matrix matrix = getMatrix();
		@NotNull List<HashSet<Index>> components;
		final int[] result = {0};

		if (matrix != null) {
			AdjacentHandler adjacentHandler = new AdjacentHandler(matrix);
			components = adjacentHandler.findAllComponents(matrix, executor, false);
			components.forEach(component -> {
				if (result[0] > -1) {
					result[0] = logic.isSubmarine(component) ? result[0] + 1 : FAIL;
				}
			});
			System.out.println(Arrays.toString(result));
		}
		try {
			objectOutputStream.writeObject(Arrays.toString(result));
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

}

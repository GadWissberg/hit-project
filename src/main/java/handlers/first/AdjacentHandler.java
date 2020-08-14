package handlers.first;

import graph.MatrixAsGraph;
import graph.Traverse;
import graph.TraverseLogic;
import handlers.BaseHandler;
import test.Index;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

public class AdjacentHandler extends BaseHandler<Index> {


	private static final String RESPONSE_RESULT = "result";

	@Override
	public void handle(final ObjectInputStream objectInputStream,
					   final ObjectOutputStream objectOutputStream,
					   final ThreadPoolExecutor executor) throws Exception {
		while (!stopped) {
			String commandString = objectInputStream.readObject().toString();
			AdjacentHandlerInputKeys command = AdjacentHandlerInputKeys.valueOf(commandString.toUpperCase());
			command.getJob().run(objectInputStream, objectOutputStream, this, executor);
		}
	}

	@Override
	public void begin(final ThreadPoolExecutor executor, final ObjectOutputStream objectOutputStream) {
		if (matrix != null) {
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
						Collection<Index> list = algorithm.findConnectedComponents(graph);
						HashSet<Index> hashSet = new HashSet<>(list);
						indexList.add(hashSet);
						seenIndexes.addAll(hashSet);
					}
				}
			}

			try {
				objectOutputStream.writeObject(indexList);
			} catch (final IOException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void stop() {

	}

	@Override
	public void setSource(final Index source) {

	}

	@Override
	public void setDestination(final Index dest) {

	}
}

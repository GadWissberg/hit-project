package handlers.routes;

import graph.MatrixAsGraph;
import graph.Traverse;
import graph.TraverseLogic;
import handlers.IHandler;
import lombok.Setter;
import test.Index;
import test.Matrix;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

@Setter
public class RoutesHandler implements IHandler {
	private Matrix matrix;
	private Index source;
	private Index dest;
	private boolean stopped;

	@Override
	public void handle(final ObjectInputStream inClient,
					   final ObjectOutputStream outClient,
					   final ThreadPoolExecutor executor) throws IOException, ClassNotFoundException {
		while (!stopped) {
			String commandString = inClient.readObject().toString();
			RoutesHandlerCommands command = RoutesHandlerCommands.valueOf(commandString.toUpperCase());
			command.getJob().run(inClient, this);
		}
	}

	void begin() {
		// Each component scan is requires ThreadLocal collections
		// However, performing multiple scans and adding each group of 1's list requires a synchronized collection
		List<HashSet<Index>> indexList = Collections.synchronizedList(new ArrayList<>());
		HashSet<Index> seenIndexes = new HashSet<>();

		Traverse<Index> algorithm = new TraverseLogic<>();
		MatrixAsGraph graph = new MatrixAsGraph(matrix, source);
		int[][] primitiveMatrix = matrix.getPrimitiveMatrix();
		for (int i = 0; i < primitiveMatrix.length; i++) {
			for (int j = 0; j < primitiveMatrix[0].length; j++) {
				final Index index = new Index(i, j);
				if (matrix.getValue(index) == 1 && !seenIndexes.contains(index)) {
					graph.setRoot(index);
					final Collection<Index> list = algorithm.traverse(graph);
					HashSet<Index> hashSet = new HashSet<>(list);
					indexList.add(hashSet);
					seenIndexes.addAll(hashSet);
				}
			}
		}
	}

	public void stop() {
		stopped = true;
	}
}

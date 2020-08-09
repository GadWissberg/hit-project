package handlers.routes;

import graph.MatrixAsGraph;
import graph.TraverseLogic;
import handlers.IHandler;
import lombok.Setter;
import test.Index;
import test.Matrix;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

@Setter
public class RoutesHandler<T> implements IHandler {
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
			command.getJob().run(inClient, this, executor);
		}
	}

	void begin(final ThreadPoolExecutor executor) throws InterruptedException {
		List<HashSet<Index>> indexList = Collections.synchronizedList(new ArrayList<>());
		HashSet<Index> seenIndices = new HashSet<>();
		TraverseLogic<Index> algorithm = new TraverseLogic<>();
		MatrixAsGraph graph = new MatrixAsGraph(matrix, source);
		RoutesResult<Index> result = algorithm.findRoutes(graph, source, dest, executor, Collections.EMPTY_SET);
		result.sort();
	}

	public void stop() {
		stopped = true;
	}
}

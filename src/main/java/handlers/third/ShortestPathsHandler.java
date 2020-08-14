package handlers.third;

import graph.MatrixAsGraph;
import graph.TraverseLogic;
import handlers.BaseHandler;
import handlers.RoutesResult;
import handlers.second.RoutesHandlerCommands;
import test.Index;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ThreadPoolExecutor;

public class ShortestPathsHandler extends BaseHandler<Index> {
	private Index source;
	private Index destination;

	@Override
	public void handle(final ObjectInputStream inClient,
					   final ObjectOutputStream outClient,
					   final ThreadPoolExecutor executor) throws IOException, ClassNotFoundException {
		while (!stopped) {
			String commandString = inClient.readObject().toString();
			RoutesHandlerCommands command = RoutesHandlerCommands.valueOf(commandString.toUpperCase());
			command.getJob().run(inClient, outClient, this, executor);
		}
	}

	@Override
	public void begin(final ThreadPoolExecutor executor, final ObjectOutputStream objectOutputStream) {
		TraverseLogic<Index> logic = new TraverseLogic<>();
		MatrixAsGraph graph = new MatrixAsGraph(matrix, source);
		RoutesResult<Index> result = logic.findShortestPaths(graph, source, destination);
	}

	@Override
	public void stop() {

	}

	@Override
	public void setSource(final Index source) {
		this.source = source;
	}

	@Override
	public void setDestination(final Index dest) {
		this.destination = dest;
	}
}

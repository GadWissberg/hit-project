package handlers.types;

import graph.MatrixAsGraph;
import graph.TraverseLogic;
import handlers.BaseHandler;
import handlers.RoutesResult;
import test.Index;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ThreadPoolExecutor;

public class ShortestPathsHandler extends BaseHandler<Index> {
	private Index source;
	private Index destination;


	@Override
	public void begin(final ThreadPoolExecutor executor, final ObjectOutputStream objectOutputStream) {
		TraverseLogic<Index> logic = new TraverseLogic<>();
		MatrixAsGraph graph = new MatrixAsGraph(getMatrix(), source);
		RoutesResult<Index> result = logic.findShortestPaths(graph, source, destination);
		try {
			objectOutputStream.writeObject(result.toString());
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {

	}

}

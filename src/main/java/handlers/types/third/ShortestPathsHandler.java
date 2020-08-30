package handlers.types.third;

import graph.MatrixAsGraph;
import handlers.BaseHandler;
import handlers.RoutesResult;
import test.Index;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ThreadPoolExecutor;

public class ShortestPathsHandler extends BaseHandler<Index> {


	@Override
	public void begin(final ThreadPoolExecutor executor, final ObjectOutputStream objectOutputStream) {
		ShortestPathsAlgorithm<Index> logic = new ShortestPathsAlgorithm<>();
		Index source = getSource();
		MatrixAsGraph graph = new MatrixAsGraph(getMatrix(), source);
		RoutesResult<Index> result = logic.findShortestPaths(graph, source, getDestination());
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

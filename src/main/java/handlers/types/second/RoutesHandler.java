package handlers.types.second;

import graph.MatrixAsGraph;
import handlers.BaseHandler;
import handlers.RoutesResult;
import lombok.Setter;
import test.Index;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.concurrent.ThreadPoolExecutor;

@Setter
public class RoutesHandler extends BaseHandler<Index> {


	@Override
	public void begin(final ThreadPoolExecutor executor, final ObjectOutputStream objectOutputStream) {
		FindRoutesAlgorithm<Index> algorithm = new FindRoutesAlgorithm<>();
		MatrixAsGraph graph = new MatrixAsGraph(getMatrix(), super.getSource());
		Index source = getSource();
		Index destination = getDestination();
		RoutesResult<Index> result = algorithm.findRoutes(graph, source, destination, executor, Collections.EMPTY_SET);
		result.sort();
		try {
			objectOutputStream.writeObject(result.toString());
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

}

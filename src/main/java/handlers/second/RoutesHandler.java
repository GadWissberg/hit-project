package handlers.second;

import graph.MatrixAsGraph;
import graph.TraverseLogic;
import handlers.BaseHandler;
import handlers.RoutesResult;
import lombok.Setter;
import test.Index;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.concurrent.ThreadPoolExecutor;

@Setter
public class RoutesHandler extends BaseHandler<Index> {
	private Index source;
	private Index dest;

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


	public void stop() {
		stopped = true;
	}

	@Override
	public void begin(final ThreadPoolExecutor executor, final ObjectOutputStream objectOutputStream) {
		TraverseLogic<Index> algorithm = new TraverseLogic<>();
		MatrixAsGraph graph = new MatrixAsGraph(matrix, source);
		RoutesResult<Index> result = algorithm.findRoutes(graph, source, dest, executor, Collections.EMPTY_SET);
//		result.sort();
		try {
			objectOutputStream.writeObject(result.toString());
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setDestination(final Index dest) {

	}
}

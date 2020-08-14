package handlers.second;

import lombok.Getter;
import test.Index;
import test.Matrix;

import java.io.IOException;

@Getter
public enum RoutesHandlerCommands {
	MATRIX((inputStream, objectOutputStream, handler, executor) -> {
		try {
			handler.setMatrix(new Matrix((int[][]) inputStream.readObject()));
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}),
	SOURCE((inputStream, objectOutputStream, handler, executor) -> {
		try {
			handler.setSource((Index) inputStream.readObject());
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}),
	DEST((inputStream, objectOutputStream, handler, executor) -> {
		try {
			handler.setDestination((Index) inputStream.readObject());
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}),
	BEGIN((inputStream, objectOutputStream, handler, executor) -> {
		handler.begin(executor, objectOutputStream);
	}),
	STOP((inputStream, objectOutputStream, handler, executor) -> {
		handler.stop();
	});

	private final Job<Index> job;

	RoutesHandlerCommands(final Job<Index> job) {
		this.job = job;
	}
}

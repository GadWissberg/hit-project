package handlers.routes;

import lombok.Getter;
import test.Index;
import test.Matrix;

import java.io.IOException;

@Getter
public enum RoutesHandlerCommands {
	MATRIX((inputStream, handler, executor) -> {
		try {
			handler.setMatrix(new Matrix((int[][]) inputStream.readObject()));
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}),
	SOURCE((inputStream, handler, executor) -> {
		try {
			handler.setSource((Index) inputStream.readObject());
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}),
	DEST((inputStream, handler, executor) -> {
		try {
			handler.setDest((Index) inputStream.readObject());
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}),
	BEGIN((inputStream, handler, executor) -> {
		try {
			handler.begin(executor);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}),
	STOP((inputStream, handler, executor) -> {
		handler.stop();
	});

	private final Job job;

	RoutesHandlerCommands(final Job job) {
		this.job = job;
	}
}

package handlers.routes;

import lombok.Getter;
import test.Index;
import test.Matrix;

import java.io.IOException;

@Getter
public enum RoutesHandlerCommands {
	MATRIX((inputStream, handler) -> {
		try {
			handler.setMatrix(new Matrix((int[][]) inputStream.readObject()));
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}),
	SOURCE((inputStream, handler) -> {
		try {
			handler.setSource((Index) inputStream.readObject());
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}),
	DEST((inputStream, handler) -> {
		try {
			handler.setDest((Index) inputStream.readObject());
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}),
	BEGIN((inputStream, handler) -> {
		handler.begin();
	}),
	STOP((inputStream, handler) -> {
		handler.stop();
	});

	private final Job job;

	RoutesHandlerCommands(final Job job) {
		this.job = job;
	}
}

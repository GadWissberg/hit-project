package handlers.first;

import handlers.second.Job;
import lombok.Getter;
import test.Index;
import test.Matrix;

import java.io.IOException;

@Getter
public enum AdjacentHandlerInputKeys {
	MATRIX((inputStream, objectOutputStream, handler, executor) -> {
		try {
			handler.setMatrix(new Matrix((int[][]) inputStream.readObject()));
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

	AdjacentHandlerInputKeys(final Job<Index> job) {
		this.job = job;
	}
}

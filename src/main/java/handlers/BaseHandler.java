package handlers;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import test.Index;
import test.Matrix;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ThreadPoolExecutor;

@Setter(AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
public abstract class BaseHandler<T> implements Handler {

	private Index source;

	private Index destination;


	@Setter(AccessLevel.NONE)
	private boolean stopped;

	private Matrix matrix;

	@Override
	public void handle(final ObjectInputStream inClient,
					   final ObjectOutputStream outClient,
					   final ThreadPoolExecutor executor) throws Exception {
		while (!stopped) {
			String commandString = inClient.readObject().toString();
			ApiCalls command = ApiCalls.valueOf(commandString.toUpperCase());
			command.getJob().run(inClient, outClient, this, executor);
		}
	}

	public abstract void begin(final ThreadPoolExecutor executor, final ObjectOutputStream objectOutputStream);

	public void stop() {
		stopped = true;
	}

}

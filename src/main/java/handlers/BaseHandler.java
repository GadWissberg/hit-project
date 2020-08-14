package handlers;

import lombok.Setter;
import test.Index;
import test.Matrix;

import java.io.ObjectOutputStream;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class BaseHandler<T> implements Handler {
	protected boolean stopped;
	@Setter
	protected Matrix matrix;

	public abstract void begin(final ThreadPoolExecutor executor, ObjectOutputStream objectOutputStream);

	public abstract void stop();

	public abstract void setSource(Index source);

	public abstract void setDestination(Index dest);
}

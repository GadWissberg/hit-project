package handlers;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ThreadPoolExecutor;

public interface Handler {
	void handle(final ObjectInputStream inClient,
				final ObjectOutputStream outClient,
				final ThreadPoolExecutor executor) throws Exception;

	void reset();
}

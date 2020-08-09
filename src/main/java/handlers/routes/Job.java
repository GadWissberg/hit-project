package handlers.routes;

import java.io.ObjectInputStream;
import java.util.concurrent.ThreadPoolExecutor;

public interface Job {
	void run(ObjectInputStream inputStream, RoutesHandler handler, ThreadPoolExecutor executor);
}

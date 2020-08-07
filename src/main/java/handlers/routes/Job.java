package handlers.routes;

import java.io.ObjectInputStream;

public interface Job {
	void run(ObjectInputStream inputStream, RoutesHandler handler);
}

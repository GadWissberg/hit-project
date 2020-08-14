package handlers.second;

import handlers.BaseHandler;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ThreadPoolExecutor;

public interface Job<T> {
	void run(ObjectInputStream inputStream, ObjectOutputStream objectOutputStream, BaseHandler<T> handler, ThreadPoolExecutor executor);
}

package handlers;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface IHandler {
	void handle(ObjectInputStream inClient, ObjectOutputStream outClient) throws Exception;
}

package test;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

public class Client {

	public static final String TASK_HEADER_TASK_1 = "task_1";
	public static final String TASK_HEADER_TASK_2 = "task_2";
	public static final String TASK_2_INPUT_SOURCE = "source";
	public static final String TASK_2_INPUT_DESTINATION = "dest";
	public static final String TASKS_INPUT_MATRIX = "matrix";
	public static final String TASKS_COMMAND_STOP = "stop";
	private static final String TASK_2_COMMAND_BEGIN = "begin";

	public static void main(final String[] args) throws IOException, ClassNotFoundException {
		Socket socket = new Socket("127.0.0.1", 8010);
		System.out.println("client::Socket");

		InputStream inputStream = socket.getInputStream();
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream toServer = new ObjectOutputStream(outputStream);
		ObjectInputStream fromServer = new ObjectInputStream(inputStream);

//		doFirstMission(toServer, fromServer);
		doSecondMission(toServer, fromServer);

		System.out.println("client::Close all streams!!!!");
		fromServer.close();
		toServer.close();
		socket.close();
		System.out.println("client::Close socket!!!!");
	}

	private static void doFirstMission(final ObjectOutputStream toServer, final ObjectInputStream fromServer)
			throws IOException, ClassNotFoundException {


		toServer.writeObject(TASK_HEADER_TASK_1);

		// sending #1 matrix
		int[][] source = {
				{0, 1, 0},
				{1, 0, 1},
				{1, 0, 1}
		};
		toServer.writeObject("matrix");
		toServer.writeObject(source);

		// sending #3 index for getAdjacentIndices
		toServer.writeObject("AdjacentIndices");
		toServer.writeObject(new Index(1, 1));
		// receiving #1 getAdjacentIndices
		Collection<Index> AdjacentIndices =
				new ArrayList<>((Collection<Index>) fromServer.readObject());
		System.out.println("client::getAdjacentIndices:: " + AdjacentIndices);

		// sending #4 index for getReachables
		toServer.writeObject("Reachables");

		toServer.writeObject(new Index(1, 1));
		// receiving #2 getReachables
		Collection<Index> ReachablesIndices =
				new ArrayList<>((Collection<Index>) fromServer.readObject());
		System.out.println("client::ReachablesIndices:: " + ReachablesIndices);

		toServer.writeObject("stop");
	}

	private static void doSecondMission(final ObjectOutputStream toServer, final ObjectInputStream fromServer)
			throws IOException, ClassNotFoundException {
		toServer.writeObject(TASK_HEADER_TASK_2);

		// sending #1 matrix
		int[][] matrix = {
				{0, 1, 1},
				{1, 0, 1},
				{1, 0, 1}
		};
		toServer.writeObject(TASKS_INPUT_MATRIX);
		toServer.writeObject(matrix);

		toServer.writeObject(TASK_2_INPUT_SOURCE);
		toServer.writeObject(new Index(0, 1));

		toServer.writeObject(TASK_2_INPUT_DESTINATION);
		toServer.writeObject(new Index(2, 2));

		toServer.writeObject(TASK_2_COMMAND_BEGIN);

		toServer.writeObject(TASKS_COMMAND_STOP);
	}
}

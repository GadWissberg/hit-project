package test;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;

public class Client {

	public static final String TASK_HEADER_TASK_1 = "task_1";
	public static final String TASK_HEADER_TASK_2 = "task_2";
	public static final String TASK_HEADER_TASK_3 = "task_3";
	public static final String TASK_HEADER_TASK_4 = "task_4";
	public static final String TASKS_INPUT_SOURCE = "source";
	public static final String TASKS_INPUT_DESTINATION = "dest";
	public static final String TASKS_INPUT_MATRIX = "matrix";
	public static final String TASKS_COMMAND_STOP = "stop";
	public static final String LOG_TASK_RESULT = "Task %s result:\n";
	private static final String TASKS_COMMAND_BEGIN = "begin";

	public static void main(final String[] args) throws IOException {
		Socket socket = new Socket("127.0.0.1", 8010);
		System.out.println("client::Socket");

		InputStream inputStream = socket.getInputStream();
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream toServer = new ObjectOutputStream(outputStream);
		ObjectInputStream fromServer = new ObjectInputStream(inputStream);

		doFirstMission(toServer, fromServer);
//		doSecondMission(toServer, fromServer);
//		doThirdMission(toServer, fromServer);
//		doFourthMission(toServer, fromServer);

		System.out.println("client::Close all streams!!!!");
		fromServer.close();
		toServer.close();
		socket.close();
		System.out.println("client::Close socket!!!!");
	}

	private static void doFirstMission(final ObjectOutputStream toServer, final ObjectInputStream fromServer)
			throws IOException {


		toServer.writeObject(TASK_HEADER_TASK_1);

		// sending #1 matrix
		int[][] source = {
				{0, 1, 0, 1},
				{1, 0, 0, 0},
				{1, 0, 1, 0},
				{0, 0, 1, 0}
		};
		toServer.writeObject(TASKS_INPUT_MATRIX);
		toServer.writeObject(source);

		toServer.writeObject(TASKS_COMMAND_BEGIN);

		try {
			List<HashSet<Index>> result = (List<HashSet<Index>>) fromServer.readObject();
			System.out.printf(LOG_TASK_RESULT, 1);
			result.forEach(System.out::println);
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		}

		toServer.writeObject(TASKS_COMMAND_STOP);


	}

	private static void doSecondMission(final ObjectOutputStream toServer, final ObjectInputStream fromServer)
			throws IOException {
		toServer.writeObject(TASK_HEADER_TASK_2);

		int[][] matrix = {
				{0, 1, 0},
				{1, 1, 1},
				{1, 0, 1},
		};
		toServer.writeObject(TASKS_INPUT_MATRIX);
		toServer.writeObject(matrix);

		toServer.writeObject(TASKS_INPUT_SOURCE);
		toServer.writeObject(new Index(2, 0));

		toServer.writeObject(TASKS_INPUT_DESTINATION);
		toServer.writeObject(new Index(2, 2));

		toServer.writeObject(TASKS_COMMAND_BEGIN);

		try {
			String result = (String) fromServer.readObject();
			System.out.printf(LOG_TASK_RESULT, 2);
			System.out.println(result);
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		}

		toServer.writeObject(TASKS_COMMAND_STOP);
	}

	private static void doThirdMission(final ObjectOutputStream toServer, final ObjectInputStream fromServer)
			throws IOException {
		toServer.writeObject(TASK_HEADER_TASK_3);

		int[][] matrix = {
				{0, 1, 1, 0},
				{1, 0, 1, 1},
				{1, 0, 1, 0},
				{1, 1, 1, 1}
		};
		toServer.writeObject(TASKS_INPUT_MATRIX);
		toServer.writeObject(matrix);

		toServer.writeObject(TASKS_INPUT_SOURCE);
		toServer.writeObject(new Index(1, 0));

		toServer.writeObject(TASKS_INPUT_DESTINATION);
		toServer.writeObject(new Index(1, 3));

		toServer.writeObject(TASKS_COMMAND_BEGIN);


		try {
			String result = (String) fromServer.readObject();
			System.out.printf(LOG_TASK_RESULT, 3);
			System.out.println(result);
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		}

		toServer.writeObject(TASKS_COMMAND_STOP);
	}

	private static void doFourthMission(final ObjectOutputStream toServer, final ObjectInputStream fromServer)
			throws IOException {
		toServer.writeObject(TASK_HEADER_TASK_4);

		int[][] matrix = {
				{1, 1, 0, 1, 1},
				{0, 0, 0, 1, 1},
				{1, 1, 0, 1, 1},
		};
		toServer.writeObject(TASKS_INPUT_MATRIX);
		toServer.writeObject(matrix);

		toServer.writeObject(TASKS_INPUT_SOURCE);
		toServer.writeObject(new Index(1, 0));

		toServer.writeObject(TASKS_INPUT_DESTINATION);
		toServer.writeObject(new Index(1, 3));

		toServer.writeObject(TASKS_COMMAND_BEGIN);


		try {
			String result = (String) fromServer.readObject();
			System.out.printf(LOG_TASK_RESULT, 3);
			System.out.println(result);
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		}

		toServer.writeObject(TASKS_COMMAND_STOP);
	}
}

package test;

import handlers.Handler;
import handlers.HandlersMapping;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {
	public static final int PORT = 8010;
	public static final int CORE_POOL_SIZE = 80;
	public static final int MAXIMUM_POOL_SIZE = 120;
	public static final int KEEP_ALIVE_TIME = 10;
	public static final String CLIENT_ACCEPTED = "A new client has connected.";
	public static final String HANDLING_CLIENT_REQUEST = "Handling client request.";
	public static final String REQUEST_DONE = "Request served. Closing sockets.";
	private final int port;
	private volatile boolean stopServer;
	private ThreadPoolExecutor executor;

	public Server(final int port) {
		this.port = port;
		stopServer = false;
		executor = null;
	}

	public static void main(final String[] args) {
		Server server = new Server(PORT);
		Map<String, Handler> handlersMapping = new HashMap<>();
		HandlersMapping[] handlers = HandlersMapping.values();
		Arrays.stream(handlers).forEach(handler -> handlersMapping.put(handler.name().toLowerCase(), handler.getHandler()));
		server.run(handlersMapping);
	}

	public void run(final Map<String, Handler> handlersMap) {

		Runnable main = () -> {
			try {
				executor = new ThreadPoolExecutor(
						CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
				ServerSocket server = new ServerSocket(port);
				server.setSoTimeout(1000);
				while (!stopServer) {
					serve(handlersMap, server);
				}
				server.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		};
		new Thread(main).start();
	}

	private void serve(final Map<String, Handler> handlersMap, final ServerSocket server) throws IOException {
		try {
			Socket request = server.accept();
			System.out.println(CLIENT_ACCEPTED);
			Runnable runnable = () -> {
				try {
					System.out.println(HANDLING_CLIENT_REQUEST);
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(request.getOutputStream());
					ObjectInputStream objectInputStream = new ObjectInputStream(request.getInputStream());
					String commandString = objectInputStream.readObject().toString();
					Handler handler = null;
					if (handlersMap.containsKey(commandString)) {
						handler = handlersMap.get(commandString);
						handler.handle(objectInputStream, objectOutputStream, executor);
						handler.reset();
						objectInputStream.close();
						objectOutputStream.close();
						System.out.println(REQUEST_DONE);
					}
					request.close();
				} catch (final Exception e) {
					System.out.println("Error: " + e.getMessage());
					System.err.println(e.getMessage());
				}
			};
			executor.execute(runnable);
		} catch (final SocketTimeoutException ignored) {
		}
	}

	public void stop() {
		if (!stopServer) {
			stopServer = true;
			if (executor != null) {
				executor.shutdown();
			}
		}
	}
}

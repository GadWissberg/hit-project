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

		Runnable mainLogic = () -> {
			try {
				executor = new ThreadPoolExecutor(
						80, 120, 10,
						TimeUnit.SECONDS, new LinkedBlockingQueue<>());
				ServerSocket server = new ServerSocket(port);
				server.setSoTimeout(1000);
				while (!stopServer) {
					try {
						Socket request = server.accept();
						System.out.println("server::client!!!!");
						Runnable runnable = () -> {
							try {
								System.out.println("server::handle!!!!");
								ObjectOutputStream objectOutputStream = new ObjectOutputStream(request.getOutputStream());
								ObjectInputStream objectInputStream = new ObjectInputStream(request.getInputStream());
								String commandString = objectInputStream.readObject().toString();
								if (handlersMap.containsKey(commandString)) {
									Handler handler = handlersMap.get(commandString);
									handler.handle(objectInputStream, objectOutputStream, executor);
								}
								System.out.println("server::Close all streams!!!!");
								// Close all streams
								objectInputStream.close();
								objectOutputStream.close();
								request.close();
							} catch (final Exception e) {
								System.out.println("server::" + e.getMessage());
								System.err.println(e.getMessage());
							}
						};
						executor.execute(runnable);
					} catch (final SocketTimeoutException ignored) {
					}
				}
				server.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		};
		new Thread(mainLogic).start();
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

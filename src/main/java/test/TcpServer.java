package test;

import handlers.HandlersMapping;
import handlers.IHandler;

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

/**
 * This class represents a multi-threaded server
 */
public class TcpServer {
	private final int port;
	private volatile boolean stopServer;
	private ThreadPoolExecutor executor;

	public TcpServer(final int port) {
		this.port = port;
		stopServer = false;
		executor = null;
	}

	public static void main(final String[] args) {
		TcpServer tcpServer = new TcpServer(8010);
		Map<String, IHandler> handlersMapping = new HashMap<>();
		HandlersMapping[] handlers = HandlersMapping.values();
		Arrays.stream(handlers).forEach(handler -> handlersMapping.put(handler.name().toLowerCase(), handler.getHandler()));
		tcpServer.run(handlersMapping);
	}

	public void run(final Map<String, IHandler> handlersMap) {

		Runnable mainLogic = () -> {
			try {
				executor = new ThreadPoolExecutor(
						3, 5, 10,
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
									IHandler handler = handlersMap.get(commandString);
									handler.handle(objectInputStream, objectOutputStream);
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

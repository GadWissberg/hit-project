package handlers.routes;

import java.util.concurrent.*;
import java.util.function.Function;

public class GenericService<T extends Runnable> {
	protected BlockingQueue<T> taskQueue;
	protected Thread consumerThread;
	protected volatile boolean isStop = false;
	protected volatile boolean isStopNow = false;
	protected Function<Runnable, T> defaultFunction;

	public GenericService(final Function<Runnable, T> runnableTFunction) {
		this(new LinkedBlockingQueue<>(), runnableTFunction);
	}

	public GenericService(final BlockingQueue<T> paramQueue,
						  final Function<Runnable, T> runnableTFunction) {
		throwIfNull(paramQueue, runnableTFunction);
		this.taskQueue = paramQueue;
		this.defaultFunction = runnableTFunction;
		this.consumerThread = new Thread(() -> {
			while (!isStop || !this.taskQueue.isEmpty() && !isStopNow) {
				try {
					taskQueue.take().run();
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		this.consumerThread.start();
	}

	public static void throwIfNull(final Object... arguments) throws NullPointerException {
		for (final Object argument : arguments) {
			if (argument == null) {
				throw new NullPointerException("arg is null");
			}
		}
	}

	public void stop(final boolean wait) throws InterruptedException {
		isStop = true;
		this.consumerThread.interrupt();
		if (wait) {
			waitUntilDone();
		}
	}

	public void waitUntilDone() throws InterruptedException {
		if (this.consumerThread.isAlive()) {
			this.consumerThread.join();
		}
	}

	public void convertAndAddToQueue(final Runnable runnable) {
		convertAndAddToQueue(runnable, defaultFunction);
	}

	public void convertAndAddToQueue(final Runnable runnable, Function<Runnable, T> runnableTFunction) {
		if (runnableTFunction == null) {
			runnableTFunction = defaultFunction;
		}
		try {
			taskQueue.offer(runnableTFunction.apply(runnable), 1000, TimeUnit.MILLISECONDS);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	public <V> Future<V> convertAndAddToQueue(final Callable<V> callable,
											  final Function<Runnable, T> runnableTFunction) {
		FutureTask<V> futureTask = new FutureTask<>(callable);
		convertAndAddToQueue(futureTask, runnableTFunction);
		return futureTask;
	}

}

import lombok.Getter;

import java.util.concurrent.*;


@Getter
public class TaskWrapper<T> implements RunnableFuture<T>, Comparable<TaskWrapper<T>> {
	private final TaskType taskType;
	private final FutureTask<T> task;


	public TaskWrapper(final TaskType taskType, final Runnable runnable) {
		this.taskType = taskType;
		this.task = new FutureTask<>(runnable, null);
	}

	public TaskWrapper(final TaskType taskType, final Callable<T> callable) {
		this.taskType = taskType;
		this.task = new FutureTask<>(callable);
	}

	@Override
	public int compareTo(final TaskWrapper<T> tTaskWrapper) {
		return Integer.compare(taskType.getPriority(), tTaskWrapper.getTaskType().getPriority());
	}

	@Override
	public String toString() {
		return "TaskWrapper{" +
				"taskType=" + taskType +
				", task=" + task +
				'}';
	}

	@Override
	public void run() {
		task.run();
	}

	@Override
	public boolean cancel(final boolean mayInterruptIfRunning) {
		return task.cancel(mayInterruptIfRunning);
	}

	@Override
	public boolean isCancelled() {
		return task.isCancelled();
	}

	@Override
	public boolean isDone() {
		return task.isDone();
	}

	@Override
	public T get() {
		T result = null;
		try {
			result = task.get();
		} catch (final InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public T get(final long l, final TimeUnit timeUnit) {
		T result = null;
		try {
			result =  task.get(l,timeUnit);
		} catch (final InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
		}
		return result;
	}

}

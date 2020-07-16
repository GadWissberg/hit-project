import lombok.Getter;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;


@Getter
public class TaskWrapper<T> extends FutureTask<T> implements Comparable<TaskWrapper<T>> {
    private final TaskType taskType;

    public TaskWrapper(final TaskType taskType, final Runnable runnable, final T result) {
        super(runnable, result);
        this.taskType = taskType;
    }

    public TaskWrapper(final TaskType taskType, final Callable<T> callable) {
        super(callable);
        this.taskType = taskType;
    }

    @Override
    public int compareTo(final TaskWrapper<T> tTaskWrapper) {
        return Integer.compare(taskType.getPriority(), tTaskWrapper.getTaskType().getPriority());
    }

    @Override
    public String toString() {
        return "TaskWrapper{" +
                "taskType=" + taskType +
                '}';
    }
}

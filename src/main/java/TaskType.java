
public enum TaskType {
    IO(1), COMPUTATIONAL(2), UNKNOWN(3);

    private int priority;

    TaskType(final int priority) {
		this.priority = priority;
	}

    public int getPriority() {
        return priority;
    }

	public void setPriority(final int priority) {
		this.priority = priority;
	}

    @Override
    public String toString() {
        return "TaskType{" +
                "priority=" + priority +
                '}';
    }
}

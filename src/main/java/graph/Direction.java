package graph;

public enum Direction {
	SOUTH(0B100),
	EAST(0B001),
	WEST(0B00),
	NORTH(0B0000);

	private final int mask;

	Direction() {
		this.mask = 0;
	}

	Direction(final int mask) {
		this.mask = mask;
	}

	public int getMask() {
		return mask;
	}
}

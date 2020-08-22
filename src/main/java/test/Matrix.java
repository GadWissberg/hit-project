package test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Matrix implements Serializable {
	int[][] primitiveMatrix;

	public Matrix(final int[][] oArray) {
		primitiveMatrix = Arrays
				.stream(oArray)
				.map(int[]::clone)
				.toArray(int[][]::new);
	}

	public static void main(final String[] args) {
		int[][] source = {
				{0, 1, 0},
				{1, 0, 1},
				{1, 0, 1}
		};
		Matrix matrix = new Matrix(source);
		matrix.printMatrix();
		System.out.println(matrix.getAxisAdjacentIndices(new Index(1, 1)));
		System.out.println(matrix.getReachables(new Index(1, 1)));
	}

	public final int[][] getPrimitiveMatrix() {
		return primitiveMatrix;
	}

	public void printMatrix() {
		for (final int[] row : primitiveMatrix) {
			String s = Arrays.toString(row);
			System.out.println(s);
		}
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (final int[] row : primitiveMatrix) {
			stringBuilder.append(Arrays.toString(row));
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}

	@SuppressWarnings("UnusedAssignment")
	public Collection<Index> getAxisAdjacentIndices(final Index index) {
		Collection<Index> list = new ArrayList<>();
		int extracted = -1;
		try {
			extracted = primitiveMatrix[index.row + 1][index.column];
			list.add(new Index(index.row + 1, index.column));
		} catch (final ArrayIndexOutOfBoundsException ignored) {
		}
		try {
			extracted = primitiveMatrix[index.row][index.column + 1];
			list.add(new Index(index.row, index.column + 1));
		} catch (final ArrayIndexOutOfBoundsException ignored) {
		}
		try {
			extracted = primitiveMatrix[index.row - 1][index.column];
			list.add(new Index(index.row - 1, index.column));
		} catch (final ArrayIndexOutOfBoundsException ignored) {
		}
		try {
			extracted = primitiveMatrix[index.row][index.column - 1];
			list.add(new Index(index.row, index.column - 1));
		} catch (final ArrayIndexOutOfBoundsException ignored) {
		}
		return list;
	}

	public int getValue(final Index index) {
		return primitiveMatrix[index.row][index.column];
	}

	public Collection<Index> getDiagonalAdjacentIndices(final Index index) {
		Collection<Index> list = new ArrayList<>();
		try {
			addIndexToDiagonalAdjacentIndices(list, index.row - 1, index.column + 1);
		} catch (final ArrayIndexOutOfBoundsException ignored) {
		}
		try {
			addIndexToDiagonalAdjacentIndices(list, index.row - 1, index.column - 1);
		} catch (final ArrayIndexOutOfBoundsException ignored) {
		}
		try {
			addIndexToDiagonalAdjacentIndices(list, index.row + 1, index.column - 1);
		} catch (final ArrayIndexOutOfBoundsException ignored) {
		}
		try {
			addIndexToDiagonalAdjacentIndices(list, index.row + 1, index.column + 1);
		} catch (final ArrayIndexOutOfBoundsException ignored) {
		}
		return list;
	}

	private void addIndexToDiagonalAdjacentIndices(final Collection<Index> list, final int row, final int column) {
		Index ind = new Index(row, column);
		getValue(ind);
		list.add(ind);
	}

	public Collection<Index> getReachables(final Index index) {
		return getReachables(index, true);
	}

	public Collection<Index> getReachables(final Index index, final boolean includeDiagonal) {
		ArrayList<Index> filteredIndices = new ArrayList<>();
		this.getAxisAdjacentIndices(index).stream().filter(i -> getValue(i) == 1).forEach(filteredIndices::add);
		if (includeDiagonal) {
			this.getDiagonalAdjacentIndices(index).stream()
					.filter(i -> getValue(i) == 1)
					.forEach(filteredIndices::add);
		}
		return filteredIndices;
	}

}
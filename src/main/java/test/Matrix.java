package test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Matrix implements Serializable {
	int[][] primitiveMatrix;

	public Matrix(int[][] oArray) {
		primitiveMatrix = Arrays
				.stream(oArray)
				.map(row -> row.clone())
				.toArray(value -> new int[value][]);
	}

	public static void main(String[] args) {
		int[][] source = {
				{0, 1, 0},
				{1, 0, 1},
				{1, 0, 1}
		};
		Matrix matrix = new Matrix(source);
		matrix.printMatrix();
		System.out.println(matrix.getAdjacentIndices(new Index(1, 1)));
		System.out.println(matrix.getReachables(new Index(1, 1)));
	}

	public void printMatrix() {
		for (int[] row : primitiveMatrix) {
			String s = Arrays.toString(row);
			System.out.println(s);
		}
	}

	public final int[][] getPrimitiveMatrix() {
		return primitiveMatrix;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int[] row : primitiveMatrix) {
			stringBuilder.append(Arrays.toString(row));
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}

	public Collection<Index> getAdjacentIndices(final Index index) {
		Collection<Index> list = new ArrayList<>();
		int extracted = -1;
		try {
			extracted = primitiveMatrix[index.row + 1][index.column];
			list.add(new Index(index.row + 1, index.column));
		} catch (ArrayIndexOutOfBoundsException ignored) {
		}
		try {
			extracted = primitiveMatrix[index.row][index.column + 1];
			list.add(new Index(index.row, index.column + 1));
		} catch (ArrayIndexOutOfBoundsException ignored) {
		}
		try {
			extracted = primitiveMatrix[index.row - 1][index.column];
			list.add(new Index(index.row - 1, index.column));
		} catch (ArrayIndexOutOfBoundsException ignored) {
		}
		try {
			extracted = primitiveMatrix[index.row][index.column - 1];
			list.add(new Index(index.row, index.column - 1));
		} catch (ArrayIndexOutOfBoundsException ignored) {
		}
		return list;
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

	public int getValue(Index index) {
		return primitiveMatrix[index.row][index.column];
	}

	public Collection<Index> getReachables(final Index index) {
		ArrayList<Index> filteredIndices = new ArrayList<>();
		this.getAdjacentIndices(index).stream()
				.filter(i -> getValue(i) == 1)
				.forEach(filteredIndices::add);
		this.getDiagonalAdjacentIndices(index).stream()
				.filter(i -> getValue(i) == 1)
				.forEach(filteredIndices::add);
		return filteredIndices;
	}
}

package test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class Matrix implements Serializable {
	int[][] primitiveMatrix;

	public Matrix(int[][] oArray) {
		primitiveMatrix = Arrays
				.stream(oArray)
				.map(row -> row.clone())
				.toArray(value -> new int[value][]);
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

	public int getValue(Index index) {
		return primitiveMatrix[index.row][index.column];
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

	public Collection<Index> getReachables(Index index) {
		ArrayList<Index> filteredIndices = new ArrayList<>();
		this.getAdjacentIndices(index).stream().filter(i -> getValue(i) == 1)
				.map(neighbor -> filteredIndices.add(neighbor)).collect(Collectors.toList());
		return filteredIndices;
	}
}
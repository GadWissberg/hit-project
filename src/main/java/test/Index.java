package test;

import java.io.Serializable;
import java.util.Objects;

public class Index implements Serializable {
	public int row, column;

	// Constructor
	public Index(final int oRow, final int oColumn) {
		this.row = oRow;
		this.column = oColumn;
	}

	public static void main(final String[] args) {
		Index myIndex = new Index(2, 2);
		System.out.println(myIndex);
	}

	@Override
	public String toString() {
		return "(" + row + "," + column + ")";
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Index index = (Index) o;
		return row == index.row &&
				column == index.column;
	}

	@Override
	public int hashCode() {
		return Objects.hash(row, column);
	}

}

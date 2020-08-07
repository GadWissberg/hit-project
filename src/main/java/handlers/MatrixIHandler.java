package handlers;

import test.Index;
import test.Matrix;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadPoolExecutor;

public class MatrixIHandler implements IHandler {

    private Matrix matrix;
    private Index start, end;

    public MatrixIHandler() {
        this.resetParams();
    }

    private void resetParams() {
        this.matrix = null;
        this.start = null;
        this.end = null;
    }

	@Override
	public void handle(final ObjectInputStream objectInputStream,
					   final ObjectOutputStream objectOutputStream,
					   final ThreadPoolExecutor executor) throws Exception {
		System.out.println("server::start handle");

		this.resetParams();

		boolean dowork = true;
		while (dowork) {
			switch (objectInputStream.readObject().toString()) {
				case "stop": {
					dowork = false;
					break;
                }
                case "matrix": {
                    int[][] primitiveMatrix = (int[][]) objectInputStream.readObject();
                    this.matrix = new Matrix(primitiveMatrix);
                    this.matrix.printMatrix();
                    break;
                }
                case "start Index": {
                    this.start = (Index) objectInputStream.readObject();
                    break;
                }
                case "end Index": {
                    this.end = (Index) objectInputStream.readObject();
                    break;
                }
                case "AdjacentIndices": {
                    // receiving index for getAdjacentIndices
                    Index indexAdjacentIndices = (Index) objectInputStream.readObject();
                    Collection<Index> adjacentIndices = new ArrayList<>();
                    if (this.matrix != null) {
						adjacentIndices.addAll(this.matrix.getAxisAdjacentIndices(indexAdjacentIndices));
                    }
                    // sending getAdjacentIndices
                    System.out.println("server::getAdjacentIndices:: " + adjacentIndices);
                    objectOutputStream.writeObject(adjacentIndices);
                    break;
                }
                case "Reachables": {
                    // receiving index for getReachables
                    Index indexReachables = (Index) objectInputStream.readObject();
                    Collection<Index> reachables = new ArrayList<>();
                    if (this.matrix != null) {
                        reachables.addAll(this.matrix.getReachables(indexReachables));
                    }
                    // sending getReachables
                    System.out.println("server::getReachables:: " + reachables);
                    objectOutputStream.writeObject(reachables);
                    break;
                }
            }
        }
    }
}

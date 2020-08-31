package handlers.types.fourth;

import graph.TraverseLogic;
import test.Index;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;

public class SubmarinesAlgorithm<T> extends TraverseLogic<T> {
	public boolean isSubmarine(HashSet<Index> component) {
		boolean result = false;
		if (component.size() > 1) {
			Object[] hashSet = component.toArray();
			Optional<Index> topMostIndex;
			topMostIndex = component.stream().min(Comparator.comparingInt(index -> index.row));

			if (topMostIndex.isPresent()) {
				Index finalTopMostIndex = topMostIndex.get();
				Optional<Index> topLeftCorner = component.stream()
						.filter(ind -> finalTopMostIndex.row >= ind.row)
						.min(Comparator.comparingInt(index -> index.column));

				result = false;
				if (topLeftCorner.isPresent()) {
					Optional<Index> bottomMostIndex = component.stream().max(Comparator.comparingInt(index -> index.row));
					Optional<Index> bottomRightCorner = component.stream()
							.filter(ind -> bottomMostIndex.get().row <= ind.row)
							.max(Comparator.comparingInt(index -> index.column));
					if (bottomRightCorner.isPresent()) {
						int rectSize = (bottomRightCorner.get().row - topLeftCorner.get().row + 1)
								* (bottomRightCorner.get().column - topLeftCorner.get().column + 1);
						result = rectSize == component.size();
					}
				}
			}
		}
		return result;
	}
}

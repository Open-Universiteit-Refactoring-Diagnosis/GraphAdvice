package nl.ou.refactoring.advice.io.layouts.globalRanking;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.edges.GraphEdge;

/**
 * Sorts graph nodes and edges by importance.
 */
public final class GraphVariationImportanceSorter {
	private GraphVariationImportanceSorter() { }

	/**
	 * Sorts variations of node-edge sequences in a graph by importance.
	 * @param variations The variations to sort.
	 * @param edgeWeights The weights assigned to edges.
	 * @return A sorted list of variations of node-edge sequences in a graph.
	 */
	public static List<GraphVariation> sort(
			Set<GraphVariation> variations,
			Map<Class<? extends GraphEdge>, Double> edgeWeights) {
		return
				variations
					.stream()
					.sorted(
							Comparator
								.comparingDouble(
										(GraphVariation variation) ->
										variation.computeImportance(edgeWeights))
								.reversed()
					)
					.collect(Collectors.toList());
	}
}

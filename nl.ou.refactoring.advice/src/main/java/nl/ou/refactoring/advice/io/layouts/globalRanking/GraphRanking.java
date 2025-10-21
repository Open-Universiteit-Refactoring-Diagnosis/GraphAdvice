package nl.ou.refactoring.advice.io.layouts.globalRanking;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * A ranking of a graph.
 */
public final class GraphRanking {
	private final Map<GraphNode, Double> globalRanking = new HashMap<>();
	
	/**
	 * Initialises a new instance of {@link GraphRanking}.
	 */
	public GraphRanking() { }

	/**
	 * Gets the global ranking of a graph's nodes.
	 * @return The global ranking of a graph's nodes.
	 */
	public Map<GraphNode, Double> getGlobalRanking() {
		return Collections.unmodifiableMap(this.globalRanking);
	}
	
	/**
	 * Updates the ranking with a variation.
	 * @param variation The variation to update the ranking with.
	 * @param edgeWeights The weights of edges.
	 */
	public void update(GraphVariation variation, Map<Class<? extends GraphEdge>, Double> edgeWeights) {
		final var contribution = variation.computeImportance(edgeWeights);
		for (final var edge : variation.getEdges()) {
			this.globalRanking.merge(edge.getSourceNode(), contribution, Double::sum);
			this.globalRanking.merge(edge.getDestinationNode(), contribution, Double::sum);
		}
	}
	
	/**
	 * Normalises the ranking by dividing ranking values by the maximum ranking value present in the ranking.
	 */
	public void normalise() {
		final var maximum =
				this
					.globalRanking
					.values()
					.stream()
					.mapToDouble(Double::doubleValue)
					.max()
					.orElse(1.0);
		if (maximum == 0.0) {
			return;
		}
		for (final var entry : this.globalRanking.entrySet()) {
			entry.setValue(entry.getValue() / maximum);
		}
	}
}

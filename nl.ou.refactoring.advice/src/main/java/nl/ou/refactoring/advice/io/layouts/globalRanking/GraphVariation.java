package nl.ou.refactoring.advice.io.layouts.globalRanking;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import nl.ou.refactoring.advice.edges.GraphEdge;

/**
 * A variation in a graph.
 */
public final class GraphVariation {
	private final List<GraphEdge> edges;
	private final int frequency;

	public GraphVariation(List<GraphEdge> edges, int frequency) {
		this.edges = edges;
		this.frequency = frequency;
	}

	public List<GraphEdge> getEdges() {
		return Collections.unmodifiableList(this.edges);
	}
	
	public int getFrequency() {
		return this.frequency;
	}
	
	public double computeImportance(Map<Class<? extends GraphEdge>, Double> edgeWeights) {
		double weightSum =
				edges
					.stream()
					.mapToDouble(edge -> edgeWeights.get(edge.getClass()))
					.sum();
		return (this.frequency * this.frequency) * weightSum;
	}
}

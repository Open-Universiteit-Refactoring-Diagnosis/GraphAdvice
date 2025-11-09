package nl.ou.refactoring.advice.io.layouts.globalRanking;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * A Global Ranking of a graph's nodes.
 * Described in "A stable graph layout algorithm for processes" [Mennens, Scheepens, Westenberg (2019)].
 */
public final class GraphGlobalRanking {
	private final Map<Class<? extends GraphNode>, Double> globalRanking;

	/**
	 * Initialises a new instance of {@link GraphGlobalRanking}.
	 */
	public GraphGlobalRanking() {
		this.globalRanking = new HashMap<Class<? extends GraphNode>, Double>();
	}

	/**
	 * Updates the ranking with a variation.
	 * @param variation The variation to update the ranking with.
	 * @param edgeWeights The weights of edges.
	 */
	public void update(GraphVariation variation, Map<Class<? extends GraphEdge>, Double> edgeWeights) {
		final var contribution = variation.computeImportance(edgeWeights);
		for (final var segment : variation.getSegments()) {
			this.globalRanking.merge(segment.getNodeClass(), contribution, Double::sum);
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
	
	/**
	 * Returns the global ranking of nodes in a graph.
	 * @return The global ranking of nodes in a graph.
	 */
	public Map<Class<? extends GraphNode>, Double> getRanking() {
		return Collections.unmodifiableMap(this.globalRanking);
	}
	
	public static GraphGlobalRanking compute(
			GraphLayoutGlobalRankingSettings settings,
			Graph graph)
					throws GraphPathSegmentInvalidException {
		final var ranking = new GraphGlobalRanking();
		
		// Extract variations
		final var variationMaximumDepth = settings.getVariationMaximumDepth();
		final var variations = GraphVariationExtractor.extract(graph, variationMaximumDepth);
		
		// Sort by importance
		final var edgeWeights = settings.getEdgeWeights();
		final var variationsSorted = GraphVariationImportanceSorter.sort(variations, edgeWeights);
		
		// Create a ranking
		for (final var variation : variationsSorted) {
			ranking.update(variation, edgeWeights);
		}
		ranking.normalise();
		return ranking;
	}
}

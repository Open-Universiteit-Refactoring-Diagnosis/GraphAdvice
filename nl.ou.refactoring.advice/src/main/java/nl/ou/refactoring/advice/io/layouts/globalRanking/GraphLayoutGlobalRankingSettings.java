package nl.ou.refactoring.advice.io.layouts.globalRanking;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.io.layouts.GraphLayoutSettings;

/**
 * Settings for a Global Ranking Layout of a Refactoring Advice Graph.
 */
public final class GraphLayoutGlobalRankingSettings extends GraphLayoutSettings {
	private Map<Class<? extends GraphEdge>, Double> edgeWeights = new HashMap<>();
	private int variationMaximumDepth = 10;
	
	/**
	 * Initialises a new instance of {@link GraphLayoutGlobalRankingSettings}.
	 */
	public GraphLayoutGlobalRankingSettings() { }

	@Override
	public GraphLayoutGlobalRanking createLayout() {
		return new GraphLayoutGlobalRanking(this);
	}
	
	/**
	 * Gets the weights of the graph's edges in a Global Ranking.
	 * @return A map of edge class types and their respective weights.
	 */
	public Map<Class<? extends GraphEdge>, Double> getEdgeWeights() {
		return Collections.unmodifiableMap(this.edgeWeights);
	}
	
	/**
	 * Sets the weights of the graph's edges in a Global Ranking.
	 * @param value A map of edge class types and their respective weights.
	 * @throws ArgumentNullException Thrown if value is null.
	 */
	public void setEdgeWeights(Map<Class<? extends GraphEdge>, Double> value)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(value, "value");
		this.edgeWeights = value;
	}
	
	/**
	 * Gets the maximum depth of a variation extracted from the graph.
	 * @return The maximum depth of a variation extracted from the graph.
	 */
	public int getVariationMaximumDepth() {
		return this.variationMaximumDepth;
	}
	
	/**
	 * Sets the maximum depth of a variation extracted from the graph.
	 * @param value The maximum depth of a variation extracted from the graph.
	 * @throws IllegalArgumentException Thrown if value is not a positive integer.
	 */
	public void setVariationMaximumDepth(int value)
			throws IllegalArgumentException {
		ArgumentGuard.requireGreaterThanOrEqual(0, value, "variationMaximumDepth");
		this.variationMaximumDepth = value;
	}
}

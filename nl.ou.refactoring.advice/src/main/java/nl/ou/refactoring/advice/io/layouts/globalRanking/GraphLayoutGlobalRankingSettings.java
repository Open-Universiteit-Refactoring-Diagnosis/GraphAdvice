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
	
	public Map<Class<? extends GraphEdge>, Double> getEdgeWeights() {
		return Collections.unmodifiableMap(this.edgeWeights);
	}
	
	public void setEdgeWeights(Map<Class<? extends GraphEdge>, Double> value)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(value, "value");
		this.edgeWeights = value;
	}
	
	public int getVariationMaximumDepth() {
		return this.variationMaximumDepth;
	}
	
	public void setVariationMaximumDepth(int value)
			throws IllegalArgumentException {
		ArgumentGuard.requireGreaterThanOrEqual(0, value, "variationMaximumDepth");
		this.variationMaximumDepth = value;
	}
}

package nl.ou.refactoring.advice.io.layouts.globalRanking;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;

/**
 * A variation in a graph.
 */
public final class GraphVariation {
	private final List<GraphVariationSegment> path;
	private final int frequency;

	/**
	 * Initialises a new instance of {@link GraphVariation}.
	 * @param path The path in the variation.
	 * @param frequency The frequency of this variation.
	 * @throws ArgumentNullException Thrown if path is null.
	 */
	public GraphVariation(List<GraphVariationSegment> path, int frequency)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(path, "path");
		this.path = path;
		this.frequency = frequency;
	}
	
	/**
	 * Gets the list of path segments in the graph variation.
	 * @return The list of path segments in the graph variation.
	 */
	public List<GraphVariationSegment> getSegments() {
		return Collections.unmodifiableList(this.path);
	}
	
	/**
	 * Gets the frequency of the variation.
	 * @return The frequency of the variation.
	 */
	public int getFrequency() {
		return this.frequency;
	}
	
	/**
	 * Computes the importance of the variation.
	 * @param edgeWeights The edge weights.
	 * @return The importance of the variation, expressed by a double-precision number.
	 */
	public double computeImportance(Map<Class<? extends GraphEdge>, Double> edgeWeights) {
		double weightSum =
				this
					.path
					.stream()
					.map(segment -> segment.getEdgeClass())
					.mapToDouble(edge -> edge == null ? 0.0 : edgeWeights.get(edge))
					.sum();
		return (this.frequency * this.frequency) * weightSum;
	}
}

package nl.ou.refactoring.advice.io.layouts.globalRanking;

import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.layouts.GraphLayout;
import nl.ou.refactoring.advice.io.layouts.GraphLayoutNode;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * A layout of a Refactoring Advice Graph using the Global Ranking algorithm.<br />
 * Described in "A stable graph layout algorithm for processes" [Mennens, Scheepens, Westenberg (2019)].
 */
public final class GraphLayoutGlobalRanking extends GraphLayout {
	/**
	 * Initialises a new instance of {@link GraphLayoutGlobalRanking}.
	 * @param settings The layout settings.
	 * @throws ArgumentNullException Thrown if settings is null.
	 */
	public GraphLayoutGlobalRanking(GraphLayoutGlobalRankingSettings settings)
			throws ArgumentNullException {
		super(settings);
	}

	@Override
	public Set<GraphLayoutNode> apply(
			Graph graph,
			Rectangle2D area)
					throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		ArgumentGuard.requireNotNull(area, "area");
		final var globalRanking = this.computeGlobalRanking(graph);
		// TODO implement the rest
		return Set.of();
	}

	@Override
	public GraphLayoutGlobalRankingSettings getSettings() {
		return (GraphLayoutGlobalRankingSettings)super.getSettings();
	}
	
	private Map<GraphNode, Double> computeGlobalRanking(Graph graph) {
		final var settings = this.getSettings();
		
		// Extract variations
		final var variationMaximumDepth = settings.getVariationMaximumDepth();
		final var variations =
				new GraphVariationExtractor()
					.extract(graph, variationMaximumDepth);
		
		// Sort by importance
		final var edgeWeights = settings.getEdgeWeights();
		final var variationsSorted = GraphVariationImportanceSorter.sort(variations, edgeWeights);
		
		// Create a ranking
		final var graphRanking = new GraphRanking();
		for (final var variation : variationsSorted) {
			graphRanking.update(variation, edgeWeights);
		}
		graphRanking.normalise();
		return graphRanking.getGlobalRanking();
	}
}

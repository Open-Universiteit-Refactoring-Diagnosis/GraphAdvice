package nl.ou.refactoring.advice.io.layouts.globalRanking;

import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
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
					throws ArgumentNullException, GraphPathSegmentInvalidException {
		ArgumentGuard.requireNotNull(graph, "graph");
		ArgumentGuard.requireNotNull(area, "area");
		final var globalRanking = GraphGlobalRanking.compute(this.getSettings(), graph);
		// TODO implement the rest
		return Set.of();
	}

	@Override
	public GraphLayoutGlobalRankingSettings getSettings() {
		return (GraphLayoutGlobalRankingSettings)super.getSettings();
	}
}

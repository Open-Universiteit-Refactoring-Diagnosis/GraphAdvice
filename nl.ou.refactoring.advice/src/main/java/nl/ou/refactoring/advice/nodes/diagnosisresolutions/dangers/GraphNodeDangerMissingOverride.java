package nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;

/**
 * Represents a "Missing Override" risk.
 */
public class GraphNodeDangerMissingOverride extends GraphNodeDanger {
	/**
	 * Initialises a new instance of {@link GraphNodeDangerMissingOverride}.
	 * @param graph The Refactoring Advice Graph to which the node belongs.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeDangerMissingOverride(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}

	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new GraphNodeDangerMissingOverride(graph);
	}
}
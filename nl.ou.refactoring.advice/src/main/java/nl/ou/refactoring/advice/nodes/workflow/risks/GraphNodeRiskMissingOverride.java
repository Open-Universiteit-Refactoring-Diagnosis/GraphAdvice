package nl.ou.refactoring.advice.nodes.workflow.risks;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * Represents a "Missing Override" risk.
 */
public class GraphNodeRiskMissingOverride extends GraphNodeRisk {
	/**
	 * Initialises a new instance of {@link GraphNodeRiskMissingOverride}.
	 * @param graph The Refactoring Advice Graph to which the node belongs.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRiskMissingOverride(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}

	@Override
	public GraphNode clone(Graph graph) {
		return new GraphNodeRiskMissingOverride(graph);
	}
}
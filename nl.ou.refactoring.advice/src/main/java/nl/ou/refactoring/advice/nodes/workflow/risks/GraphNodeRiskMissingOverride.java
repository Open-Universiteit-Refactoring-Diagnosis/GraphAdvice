package nl.ou.refactoring.advice.nodes.workflow.risks;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Represents a "Missing Override" risk.
 */
public class GraphNodeRiskMissingOverride extends GraphNodeRisk {

	public GraphNodeRiskMissingOverride(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}

}

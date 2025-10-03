package ou.graphAdvice.nodes.refactoring.risks;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentNullException;

/**
 * Represents a "Missing Definition" risk in a Refactoring Advice Graph.
 * This risk may arise if a method is still called after it has been (re)moved.
 */
public final class GraphNodeRiskMissingDefinition extends GraphNodeRisk {
	/**
	 * Initialises a new instance of {@link GraphNodeRiskMissingDefinition}.
	 * @param graph {@link Graph} The graph that contains the risk.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRiskMissingDefinition(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
}

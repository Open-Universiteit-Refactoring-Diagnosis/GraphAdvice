package ou.graphAdvice.nodes.refactoring.risks;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentNullException;

/**
 * Represents a "Missing Abstract Implementation" risk in a Refactoring Advice Graph.
 * This risk may arise if a method that is removed implements an abstract method and no other overrides exist.
 */
public final class GraphNodeRiskMissingAbstractImplementation extends GraphNodeRisk {
	/**
	 * Initialises a new instance of {@link GraphNodeRiskMissingAbstractImplementation}.
	 * @param graph {@link Graph} The graph that contains the risk.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRiskMissingAbstractImplementation(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
}

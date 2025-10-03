package ou.graphAdvice.nodes.refactoring.risks;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentNullException;

/**
 * Represents a "Remove Concrete Override" risk in a Refactoring Advice Graph.
 * This risk may arise if an overriding method is removed, causing a call to the method to fall back to a generalised implementation.
 */
public final class GraphNodeRiskRemoveConcreteOverride extends GraphNodeRisk {
	/**
	 * Initialises a new instance of {@link GraphNodeRiskRemoveConcreteOverride}.
	 * @param graph {@link Graph} The graph that contains the risk.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRiskRemoveConcreteOverride(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
}

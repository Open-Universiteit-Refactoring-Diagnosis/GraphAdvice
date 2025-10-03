package ou.graphAdvice.nodes.refactoring.risks;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentNullException;

/**
 * Represents a "Missing Super Implementation" risk in a Refactoring Advice Graph.
 * This risk may arise if an overriding method is removed and none of the subclasses contain an alternative implementation.
 */
public final class GraphNodeRiskMissingSuperImplementation extends GraphNodeRisk {
	/**
	 * Initialises a new instance of {@link GraphNodeRiskMissingSuperImplementation}.
	 * @param graph {@link Graph} The graph that contains the risk.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRiskMissingSuperImplementation(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}

}

package ou.graphAdvice.nodes.refactoring.risks;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentNullException;

/**
 * Represents a "Lost Specification" risk in a Refactoring Advice Graph.
 * This risk may arise if a method that specifies the root of a tree of overriding methods is removed.
 */
public final class GraphNodeRiskLostSpecification extends GraphNodeRisk {
	/**
	 * Initialises a new instance of {@link GraphNodeRiskLostSpecification}.
	 * @param graph {@link Graph} The graph that contains the risk.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRiskLostSpecification(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
}

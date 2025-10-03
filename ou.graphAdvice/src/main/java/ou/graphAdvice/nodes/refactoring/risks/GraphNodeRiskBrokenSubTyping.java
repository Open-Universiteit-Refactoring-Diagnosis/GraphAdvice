package ou.graphAdvice.nodes.refactoring.risks;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentNullException;

/**
 * Represents a "Broken Sub-typing" risk in a Refactoring Advice Graph.
 * This risk may arise if a method is inserted into the inheritance / override hierarchy of a class's method and thus changes its behaviour.
 */
public final class GraphNodeRiskBrokenSubTyping extends GraphNodeRisk {
	/**
	 * Initialises a new instance of {@link GraphNodeRiskBrokenSubTyping}.
	 * @param graph {@link Graph} The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRiskBrokenSubTyping(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
}

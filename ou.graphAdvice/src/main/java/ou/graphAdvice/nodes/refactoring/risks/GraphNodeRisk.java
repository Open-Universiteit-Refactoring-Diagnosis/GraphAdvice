package ou.graphAdvice.nodes.refactoring.risks;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentNullException;
import ou.graphAdvice.nodes.refactoring.GraphNodeRefactoring;

/**
 * Represents a risk in a Refactoring Advice Graph.
 */
public abstract class GraphNodeRisk extends GraphNodeRefactoring {
	/**
	 * Initialises a new instance of {@link GraphNodeRisk}.
	 * @param graph The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRisk(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
}

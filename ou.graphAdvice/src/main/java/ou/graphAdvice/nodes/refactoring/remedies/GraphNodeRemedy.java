package ou.graphAdvice.nodes.refactoring.remedies;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentNullException;
import ou.graphAdvice.nodes.refactoring.GraphNodeRefactoring;

/**
 * Represents a remedy in a Refactoring Advice Graph.
 */
public abstract class GraphNodeRemedy extends GraphNodeRefactoring {
	/**
	 * Initialises a new instance of {@link GraphNodeRemedy}.
	 * @param graph The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRemedy(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
}

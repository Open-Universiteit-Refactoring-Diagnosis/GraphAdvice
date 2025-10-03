package ou.graphAdvice.nodes.refactoring;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentNullException;
import ou.graphAdvice.nodes.GraphNode;

/**
 * Represents a node in a Refactoring Advice Graph that describes a component of a refactoring.
 */
public abstract class GraphNodeRefactoring extends GraphNode {
	/**
	 * Initialises a new instance of {@link GraphNodeRefactoring}.
	 * @param graph The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	protected GraphNodeRefactoring(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
}

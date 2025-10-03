package ou.graphAdvice.nodes.code;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentNullException;
import ou.graphAdvice.nodes.GraphNode;

/**
 * Represents a node in a Refactoring Advice Graph that describes the affected programme code.
 */
public abstract class GraphNodeCode extends GraphNode {
	/**
	 * Initialises a new instance of {@link GraphNodeCode}.
	 * @param graph The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	protected GraphNodeCode(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
}

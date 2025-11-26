package nl.ou.refactoring.advice.nodes.code;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * Represents a node in a Refactoring Advice Graph that describes the affected programme code.
 */
public abstract class GraphNodeCode extends GraphNode {
	/**
	 * Initialises a new instance of {@link GraphNodeCode}.
	 * @param graph The graph that contains the node.
	 * @throws NullPointerException Thrown if graph is null.
	 */
	protected GraphNodeCode(Graph graph)
			throws NullPointerException {
		super(graph);
	}
}

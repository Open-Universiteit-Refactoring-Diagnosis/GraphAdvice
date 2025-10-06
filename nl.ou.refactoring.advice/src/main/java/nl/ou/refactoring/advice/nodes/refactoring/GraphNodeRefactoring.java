package nl.ou.refactoring.advice.nodes.refactoring;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.GraphNode;

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

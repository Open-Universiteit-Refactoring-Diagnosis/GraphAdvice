package nl.ou.refactoring.advice.nodes.workflow;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * Represents a node in a Refactoring Advice Graph that describes a component of a refactoring.
 */
public abstract class GraphNodeWorkflow extends GraphNode {
	/**
	 * Initialises a new instance of {@link GraphNodeWorkflow}.
	 * @param graph The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	protected GraphNodeWorkflow(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
}

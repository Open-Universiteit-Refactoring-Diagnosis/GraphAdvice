package nl.ou.refactoring.advice.nodes.refactoring.remedies;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.refactoring.GraphNodeRefactoring;

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

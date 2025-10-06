package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Represents a microstep in a Refactoring Advice Graph that removes a method.
 */
public final class GraphNodeMicrostepRemoveMethod extends GraphNodeMicrostep {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepRemoveMethod}.
	 * @param graph The graph that contains the node. Cannot be null.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepRemoveMethod(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
}

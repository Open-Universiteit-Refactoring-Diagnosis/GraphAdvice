package ou.graphAdvice.nodes.refactoring.microsteps;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentNullException;

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

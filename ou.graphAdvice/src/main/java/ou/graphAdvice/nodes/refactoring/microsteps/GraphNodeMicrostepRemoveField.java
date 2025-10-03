package ou.graphAdvice.nodes.refactoring.microsteps;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentNullException;

/**
 * A "Remove Field" microstep.
 */
public final class GraphNodeMicrostepRemoveField extends GraphNodeMicrostep {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepRemoveField}.
	 * @param graph The graph that contains the microstep. Cannot be null.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepRemoveField(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
}

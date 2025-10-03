package ou.graphAdvice.nodes.refactoring.microsteps;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentNullException;

/**
 * A "Remove Class" microstep.
 */
public final class GraphNodeMicrostepRemoveClass extends GraphNodeMicrostep {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepRemoveClass}.
	 * @param graph The graph that contains the microstep. Cannot be null.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepRemoveClass(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
}

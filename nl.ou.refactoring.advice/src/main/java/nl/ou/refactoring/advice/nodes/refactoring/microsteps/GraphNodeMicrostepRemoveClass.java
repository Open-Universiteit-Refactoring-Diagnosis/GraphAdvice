package nl.ou.refactoring.advice.nodes.refactoring.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

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

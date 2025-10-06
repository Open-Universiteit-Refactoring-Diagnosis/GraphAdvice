package nl.ou.refactoring.advice.nodes.refactoring.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

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

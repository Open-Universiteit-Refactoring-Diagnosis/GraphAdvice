package ou.graphAdvice.nodes.refactoring.microsteps;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentNullException;

/**
 * An "Add Class" microstep.
 */
public final class GraphNodeMicrostepAddClass extends GraphNodeMicrostep {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepAddClass}.
	 * @param graph The graph that contains the microstep. Cannot be null.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepAddClass(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
}

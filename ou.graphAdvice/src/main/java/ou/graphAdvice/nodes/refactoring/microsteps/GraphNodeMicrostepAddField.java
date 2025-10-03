package ou.graphAdvice.nodes.refactoring.microsteps;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentNullException;

/**
 * An "Add Field" microstep.
 */
public final class GraphNodeMicrostepAddField extends GraphNodeMicrostep {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepAddField}.
	 * @param graph The graph that contains the microstep. Cannot be null.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepAddField(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
}

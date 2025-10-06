package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * An "Update References" microstep.
 */
public final class GraphNodeMicrostepUpdateReferences extends GraphNodeMicrostep {

	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepUpdateReferences}.
	 * @param graph The graph that contains the microstep. Cannot be null.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepUpdateReferences(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
}

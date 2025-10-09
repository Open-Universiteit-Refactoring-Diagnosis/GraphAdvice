package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Represents a Microstep in a Refactoring Advice Graph that removes a Field.
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

	@Override
	public String getCaption() {
		return "Remove Field";
	}
}

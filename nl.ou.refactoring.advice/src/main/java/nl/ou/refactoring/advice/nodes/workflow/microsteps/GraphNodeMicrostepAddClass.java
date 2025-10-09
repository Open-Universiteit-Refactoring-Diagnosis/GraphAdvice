package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Represents a Microstep in a Refactoring Advice Graph that adds a Class.
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

	@Override
	public String getCaption() {
		return "Add Class";
	}
}

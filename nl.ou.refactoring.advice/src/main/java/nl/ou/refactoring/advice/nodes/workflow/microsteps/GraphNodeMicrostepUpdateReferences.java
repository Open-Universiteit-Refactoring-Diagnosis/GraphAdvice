package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Represents a Microstep in a Refactoring Advice Graph that updates references of one code symbol to another.
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

	@Override
	public String getCaption() {
		return "Update References";
	}
}

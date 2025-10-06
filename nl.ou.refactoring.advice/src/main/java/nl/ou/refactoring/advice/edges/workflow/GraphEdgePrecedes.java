package nl.ou.refactoring.advice.edges.workflow;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostep;

/**
 * An edge that indicates that a microstep precedes another microstep in a refactoring.
 */
public final class GraphEdgePrecedes extends GraphEdge {
	/**
	 * Initialises a new instance of {@link GraphEdgePrecedes}.
	 * @param currentMicrostep The current microstep.
	 * @param nextMicrostep The next current microstep.
	 * @throws ArgumentNullException Thrown if currentMicrostep or nextMicrostep is null.
	 */
	public GraphEdgePrecedes(GraphNodeMicrostep currentMicrostep, GraphNodeMicrostep nextMicrostep)
			throws ArgumentNullException {
		super(currentMicrostep, nextMicrostep);
	}
}

package ou.graphAdvice.edges.refactoring;

import ou.graphAdvice.contracts.ArgumentNullException;
import ou.graphAdvice.edges.GraphEdge;
import ou.graphAdvice.nodes.refactoring.microsteps.GraphNodeMicrostep;

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
